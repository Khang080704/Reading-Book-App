package org.example.bookreadingapp.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookreadingapp.client.BookApiClient;
import org.example.bookreadingapp.dto.book.BookDetailDTO;
import org.example.bookreadingapp.dto.book.EditionDTO;
import org.example.bookreadingapp.dto.book.EditionsListDTO;
import org.example.bookreadingapp.dto.book.OpenLibraryEditionDTO;
import org.example.bookreadingapp.dto.book.OpenLibraryEditionsDTO;
import org.example.bookreadingapp.dto.book.OpenLibraryWorkDTO;
import org.example.bookreadingapp.dto.book.SearchBookDTO;
import org.example.bookreadingapp.dto.book.SearchBooksDTO;
import org.example.bookreadingapp.entity.AuthorDetail;
import org.example.bookreadingapp.entity.Edition;
import org.example.bookreadingapp.entity.Work;
import org.example.bookreadingapp.exception.definitions.BookApiException;
import org.example.bookreadingapp.exception.definitions.BookNotFound;
import org.example.bookreadingapp.repository.AuthorDetailRepository;
import org.example.bookreadingapp.repository.EditionRepository;
import org.example.bookreadingapp.repository.WorkRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {
    private final BookApiClient bookApiClient;
    private final WorkRepository workRepository;
    private final EditionRepository editionRepository;
    private final AuthorDetailRepository authorDetailRepository;

    @Cacheable(value = "bookSearch", key = "#query + '-' + #page + '-' + #limit")
    public List<SearchBookDTO> searchBooks(String query, int page, int limit) {
        log.info("Cache miss! call open library api");
        SearchBooksDTO data = bookApiClient.searchBooks(query, page, limit);
        return mapEntriesToSearchDTO(data != null ? data.getDocs() : null);
    }

    public BookDetailDTO getWorkDetails(String workKey) {
        String canonicalWorkKey = canonicalWorkKey(workKey);

        return workRepository.findByWorkKey(canonicalWorkKey)
                .map(this::mapWorkToDetailDTO)
                .orElseGet(() -> mapWorkToDetailDTO(persistWorkFromApi(canonicalWorkKey)));
    }

    public EditionsListDTO getWorkEditions(String workKey) {
        String canonicalWorkKey = canonicalWorkKey(workKey);

        List<Edition> cachedEditions = editionRepository.findByWork_WorkKey(canonicalWorkKey);
        if (!cachedEditions.isEmpty()) {
            return EditionsListDTO.builder()
                    .workKey(canonicalWorkKey)
                    .editions(cachedEditions.stream().map(this::mapEditionToDTO).collect(Collectors.toList()))
                    .build();
        }

        Work work = workRepository.findByWorkKey(canonicalWorkKey)
                .orElseGet(() -> persistWorkFromApi(canonicalWorkKey));

        OpenLibraryEditionsDTO response = fetchWorkEditions(canonicalWorkKey);
        List<EditionDTO> editions = persistWorkEditions(work, response != null ? response.getEntries() : null);

        return EditionsListDTO.builder()
                .workKey(canonicalWorkKey)
                .editions(editions)
                .build();
    }

    public EditionDTO getEditionDetails(String editionKey) {
        String canonicalEditionKey = canonicalEditionKey(editionKey);

        return editionRepository.findByEditionKey(canonicalEditionKey)
                .map(this::mapEditionToDTO)
                .orElseGet(() -> mapEditionToDTO(persistEditionFromApi(canonicalEditionKey)));
    }

    private List<SearchBookDTO> mapEntriesToSearchDTO(List<SearchBooksDTO.BookSearchEntry> docs) {
        if (docs == null) {
            return Collections.emptyList();
        }

        return docs.stream()
                .map(entry -> {
                    String id = entry.getKey().substring(entry.getKey().lastIndexOf("/") + 1);
                    return SearchBookDTO.builder()
                            .bookKey(entry.getKey())
                            .title(entry.getTitle())
                            .authorNames(entry.getAuthorNames() != null
                                    ? entry.getAuthorNames().toArray(new String[0])
                                    : new String[0])
                            .firstPublishYear(entry.getFirstPublishYear())
                            .isbn(entry.getFirstIsbn())
                            .editionCount(entry.getEditionCount())
                            .coverUrl("https://covers.openlibrary.org/b/id/" + id + "-M.jpg")
                            .build();
                })
                .collect(Collectors.toList());
    }

    private Work persistWorkFromApi(String canonicalWorkKey) {
        OpenLibraryWorkDTO response = fetchWorkDetail(canonicalWorkKey);
        return persistWork(response);
    }

    private OpenLibraryEditionsDTO fetchWorkEditions(String canonicalWorkKey) {
        try {
            return bookApiClient.getWorkEditions(toApiId(canonicalWorkKey));
        } catch (FeignException.NotFound ex) {
            throw new BookNotFound("Work with key " + canonicalWorkKey + " not found.", ex);
        } catch (FeignException ex) {
            throw new BookApiException("Failed to fetch work editions: " + ex.contentUTF8(), ex.status(), ex);
        }
    }

    private OpenLibraryWorkDTO fetchWorkDetail(String canonicalWorkKey) {
        try {
            return bookApiClient.getWorkDetail(toApiId(canonicalWorkKey));
        } catch (FeignException.NotFound ex) {
            throw new BookNotFound("Work with key " + canonicalWorkKey + " not found.", ex);
        } catch (FeignException ex) {
            throw new BookApiException("Failed to fetch work details: " + ex.contentUTF8(), ex.status(), ex);
        }
    }

    private OpenLibraryEditionDTO fetchEditionDetail(String canonicalEditionKey) {
        try {
            return bookApiClient.getEditionDetail(toApiId(canonicalEditionKey));
        } catch (FeignException.NotFound ex) {
            throw new BookNotFound("Edition with key " + canonicalEditionKey + " not found.", ex);
        } catch (FeignException ex) {
            throw new BookApiException("Failed to fetch edition details: " + ex.contentUTF8(), ex.status(), ex);
        }
    }

    private Work persistWork(OpenLibraryWorkDTO response) {
        if (response == null || response.getKey() == null || response.getKey().isBlank()) {
            throw new BookApiException("Open Library work payload is empty.", 502);
        }

        Work work = Work.builder()
                .workKey(canonicalWorkKey(response.getKey()))
                .title(response.getTitle())
                .description(response.getDescription())
                .coverId(response.getCoverId())
                .authors(resolveAuthors(response.getAuthorKeys()))
                .build();

        return workRepository.save(work);
    }

    private Edition persistEditionFromApi(String canonicalEditionKey) {
        OpenLibraryEditionDTO response = fetchEditionDetail(canonicalEditionKey);
        Work work = resolveWorkForEdition(response);
        return persistEdition(work, response);
    }

    private Work resolveWorkForEdition(OpenLibraryEditionDTO response) {
        String workKey = response != null ? response.getWorkKey() : null;
        if (workKey == null || workKey.isBlank()) {
            throw new BookApiException("Open Library edition payload does not contain work reference.", 502);
        }

        String canonicalWorkKey = canonicalWorkKey(workKey);
        return workRepository.findByWorkKey(canonicalWorkKey)
                .orElseGet(() -> persistWorkFromApi(canonicalWorkKey));
    }

    private List<EditionDTO> persistWorkEditions(Work work, List<OpenLibraryEditionsDTO.EditionEntry> entries) {
        if (entries == null || entries.isEmpty()) {
            return Collections.emptyList();
        }

        return entries.stream()
                .map(entry -> persistEdition(work, entry))
                .map(this::mapEditionToDTO)
                .collect(Collectors.toList());
    }

    private Edition persistEdition(Work work, OpenLibraryEditionsDTO.EditionEntry entry) {
        String canonicalEditionKey = canonicalEditionKey(entry != null ? entry.getKey() : null);
        if (canonicalEditionKey == null || canonicalEditionKey.isBlank()) {
            throw new BookApiException("Open Library edition payload is missing edition key.", 502);
        }

        Edition edition = editionRepository.findByEditionKey(canonicalEditionKey)
                .orElseGet(() -> Edition.builder()
                        .editionKey(canonicalEditionKey)
                        .isbn(entry.getIsbn())
                        .numberOfPages(entry.getNumberOfPages())
                        .publishDate(entry.getPublishDate())
                        .publisherName(entry.getPublisherName())
                        .work(work)
                        .build());

        if (edition.getWork() == null) {
            edition.setWork(work);
        }

        return editionRepository.save(edition);
    }

    private Edition persistEdition(Work work, OpenLibraryEditionDTO response) {
        String canonicalEditionKey = canonicalEditionKey(response != null ? response.getKey() : null);
        if (canonicalEditionKey == null || canonicalEditionKey.isBlank()) {
            throw new BookApiException("Open Library edition payload is missing edition key.", 502);
        }

        Edition edition = editionRepository.findByEditionKey(canonicalEditionKey)
                .orElseGet(() -> Edition.builder()
                        .editionKey(canonicalEditionKey)
                        .isbn(response.getIsbn())
                        .numberOfPages(response.getNumberOfPages())
                        .publishDate(response.getPublishDate())
                        .publisherName(response.getPublisherName())
                        .work(work)
                        .build());

        if (edition.getWork() == null) {
            edition.setWork(work);
        }

        return editionRepository.save(edition);
    }

    private Set<AuthorDetail> resolveAuthors(List<String> authorKeys) {
        if (authorKeys == null || authorKeys.isEmpty()) {
            return Collections.emptySet();
        }

        Map<String, AuthorDetail> authorMap = new LinkedHashMap<>();
        for (String authorKey : authorKeys) {
            String canonicalAuthorKey = canonicalAuthorKey(authorKey);
            if (canonicalAuthorKey == null || canonicalAuthorKey.isBlank()) {
                continue;
            }

            authorMap.computeIfAbsent(canonicalAuthorKey, key ->
                    authorDetailRepository.findByOlKey(key)
                            .orElseGet(() -> AuthorDetail.builder()
                                    .olKey(key)
                                    .build()));
        }

        return new HashSet<>(authorMap.values());
    }

    private BookDetailDTO mapWorkToDetailDTO(Work work) {
        return BookDetailDTO.builder()
                .workKey(work.getWorkKey())
                .title(work.getTitle())
                .description(work.getDescription())
                .coverUrl(work.getCoverId() != null ? "https://covers.openlibrary.org/b/id/" + work.getCoverId() + "-M.jpg" : null)
                .authorKeys(work.getAuthors() != null
                        ? work.getAuthors().stream()
                        .map(AuthorDetail::getOlKey)
                        .filter(key -> key != null && !key.isBlank())
                        .collect(Collectors.toList())
                        : Collections.emptyList())
                .build();
    }

    private EditionDTO mapEditionToDTO(Edition edition) {
        return EditionDTO.builder()
                .editionKey(edition.getEditionKey())
                .isbn(edition.getIsbn())
                .numberOfPages(edition.getNumberOfPages())
                .publishDate(edition.getPublishDate())
                .publisherName(edition.getPublisherName())
                .build();
    }

    private String canonicalWorkKey(String workKey) {
        String apiId = toApiId(workKey);
        return apiId == null || apiId.isBlank() ? null : "/works/" + apiId;
    }

    private String canonicalEditionKey(String editionKey) {
        String apiId = toApiId(editionKey);
        return apiId == null || apiId.isBlank() ? null : "/editions/" + apiId;
    }

    private String canonicalAuthorKey(String authorKey) {
        String apiId = toApiId(authorKey);
        return apiId == null || apiId.isBlank() ? null : "/authors/" + apiId;
    }

    private String toApiId(String key) {
        if (key == null) {
            return null;
        }

        String normalized = key.trim();
        if (normalized.isEmpty()) {
            return normalized;
        }

        if (normalized.startsWith("/")) {
            normalized = normalized.substring(1);
        }

        int lastSlash = normalized.lastIndexOf('/');
        if (lastSlash >= 0) {
            normalized = normalized.substring(lastSlash + 1);
        }

        return normalized;
    }
}
