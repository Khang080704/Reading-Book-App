package org.example.bookreadingapp.service.provider;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookreadingapp.client.AuthorApiClient;
import org.example.bookreadingapp.client.BookApiClient;
import org.example.bookreadingapp.dto.book.*;
import org.example.bookreadingapp.entity.AuthorDetail;
import org.example.bookreadingapp.entity.Work;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component("openlibrary")
@RequiredArgsConstructor
@Slf4j
@Primary
public class OpenLibraryProvider implements SearchProvider<SearchBooksDTO.BookSearchEntry> {
    private final BookApiClient bookApiClient;
    private final AuthorApiClient authorApiClient;

    @Override
    public List<SearchBookDTO> search(String query, int page, int limit) {
        try {
            SearchBooksDTO data = bookApiClient.searchBooks(query, page, limit);
            return mapEntriesToSearchDTO(data != null ? data.getDocs() : null);
        }
        catch (FeignException ex) {
            log.error("Error occurred while searching for books with Open Library: {}", ex.getMessage(), ex);
            throw ex;
        }

    }

    @Override
    public List<SearchBookDTO> mapEntriesToSearchDTO(List<SearchBooksDTO.BookSearchEntry> docs) {
        if (docs == null) {
            return Collections.emptyList();
        }

        return docs.stream()
                .map(entry -> SearchBookDTO.builder()
                        .bookKey(entry.getKey())
                        .title(entry.getTitle())
                        .authorNames(entry.getAuthorNames() != null
                                ? entry.getAuthorNames().toArray(new String[0])
                                : new String[0])
                        .firstPublishYear(entry.getFirstPublishYear())
                        .isbn(entry.getFirstIsbn())
                        .editionCount(entry.getEditionCount())
                        .coverUrl(entry.getCoverUrl())
                        .build())
                .collect(Collectors.toList());

    }

    @Override
    public ProviderWorkPage getWorksByAuthor(AuthorDetail authorDetail, int offset, int limit) {
        AuthorWorksDTO data = authorApiClient.getAuthorWorks(authorDetail.getOlKey(), offset, limit);
        List<WorkDTO> works = mapAuthorWorksToDto(data);

        boolean hasNext = works.size() == limit;

        return new ProviderWorkPage(works, hasNext, offset + works.size(), data.getSize());
    }

    private List<WorkDTO> mapAuthorWorksToDto (AuthorWorksDTO authorWorksDTO) {
        return authorWorksDTO.getEntries().stream()
                .map(entry -> WorkDTO.builder()
                        .workKey(toApiId(entry.getKey()))
                        .title(entry.getTitle())
                        .description(entry.getDescription())
                        .coverUrl(entry.getCoverId() != null ?
                                "https://covers.openlibrary.org/b/id/" + entry.getCoverId() + "-M.jpg"
                                : null)
                        .build())
                .collect(Collectors.toList());
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
