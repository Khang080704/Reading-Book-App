package org.example.bookreadingapp.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookreadingapp.client.AuthorApiClient;
import org.example.bookreadingapp.dto.author.*;
import org.example.bookreadingapp.dto.book.AuthorWorksDTO;
import org.example.bookreadingapp.dto.book.WorkDTO;
import org.example.bookreadingapp.entity.Author;
import org.example.bookreadingapp.entity.AuthorDetail;
import org.example.bookreadingapp.exception.definitions.AuthorNotExists;
import org.example.bookreadingapp.repository.AuthorRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorApiClient authorApiClient;

    public List<AuthorDTO> searchAuthors(String authorName) {
        List<Author> result = authorRepository.findByNameContainingIgnoreCase(authorName);
        if(!result.isEmpty()){
            return getAuthorDTOS(result);
        }

        AuthorListResponse response = authorApiClient.getAuthors(authorName);

        if(response.getNumFound() == 0 || response.getDocs().isEmpty()) {
            return getAuthorDTOS(result);
        }

        for(OpenLibraryAuthorDTO author : response.getDocs()) {
            Optional<Author> existingAuthor = authorRepository.findByOlKey(author.getKey());
            if(existingAuthor.isPresent()) {
                result.add(existingAuthor.get());
            }
            else {
                Author authorEntity = new Author();
                authorEntity.setName(author.getName());
                authorEntity.setBirthDay(author.getBirthDay());
                authorEntity.setOlKey(author.getKey());
                authorEntity.setReadCount(author.getReadingCount());
                authorRepository.save(authorEntity);
                result.add(authorEntity);
            }


        }

        return getAuthorDTOS(result);
    }

    private List<AuthorDTO> getAuthorDTOS(List<Author> result) {
        return result.stream().map(author -> {
            AuthorDTO dto = new AuthorDTO();
            dto.setId(author.getId());
            dto.setName(author.getName());
            dto.setBirthDay(author.getBirthDay());
            dto.setReadCount(author.getReadCount());
            dto.setOlKey(author.getOlKey());
            dto.setAvatar("https://covers.openlibrary.org/a/olid/" + author.getOlKey() + "-M.jpg");
            return dto;
        }).toList();
    }

    public List<AuthorDTO> getAuthors(PageRequest pageRequest) {
        List<Author> authorList = authorRepository.findAll(pageRequest).getContent();
        log.info("Fetched authors from database: {}", authorList);
        return getAuthorDTOS(authorList);
    }

    public AuthorDetailDTO getAuthorDetail(String olKey) throws AuthorNotExists {
        Optional<Author> author = authorRepository.findByOlKey(olKey);
        if(author.isPresent()) {
            AuthorDetailDTO detailDTO = new AuthorDetailDTO();
            Author authorEntity = author.get();

            if(authorEntity.getAuthorDetail() != null) {
                detailDTO.setBio(authorEntity.getAuthorDetail().getBio());
                detailDTO.setCreatedAt(authorEntity.getAuthorDetail().getCreatedAt());
                detailDTO.setFullName(authorEntity.getAuthorDetail().getFullName());
                detailDTO.setBirthDate(authorEntity.getAuthorDetail().getBirthDay());
                detailDTO.setLastModifiedAt(authorEntity.getAuthorDetail().getLastModify());
                return detailDTO;
            }

            AuthorDetailResponse authorDetailResponse = authorApiClient.getAuthorDetail(olKey);
            log.info("Fetched author detail from API for olKey {}: {}", olKey, authorDetailResponse);
            log.info("Bio length: {}", authorDetailResponse.getBio().length());

            AuthorDetail authorDetail = new AuthorDetail();
            authorDetail.setBirthDay(authorDetailResponse.getBirthDate());
            authorDetail.setBio(authorDetailResponse.getBio() == null ? "Bio is updating" : authorDetailResponse.getBio());
            authorDetail.setFullName(authorDetailResponse.getFullName());
            authorDetail.setCreatedAt(LocalDateTime.parse(authorDetailResponse.getCreatedAt()));
            authorDetail.setLastModify(LocalDateTime.parse(authorDetailResponse.getLastModifiedAt()));

            authorEntity.setAuthorDetail(authorDetail);

            detailDTO.setBio(authorDetail.getBio());
            detailDTO.setFullName(authorDetail.getFullName());
            detailDTO.setBirthDate(authorDetail.getBirthDay());
            detailDTO.setCreatedAt(authorDetail.getCreatedAt());
            detailDTO.setLastModifiedAt(authorDetail.getLastModify());

            authorRepository.save(authorEntity);


            return detailDTO;
        }
        else {
            throw new AuthorNotExists("Author with olKey " + olKey + " does not exist.");
        }
    }

    /**
     * Get works by author from Open Library
     * Uses Author Works API: /authors/{authorKey}/works.json
     * https://openlibrary.org/dev/docs/api/authors
     */
    public List<WorkDTO> getAuthorWorks(String authorKey) throws AuthorNotExists {
        log.info("Fetching works for author: {}", authorKey);

        // Normalize author key
        String normalizedKey = normalizeAuthorKey(authorKey);

        try {
            AuthorWorksDTO worksResponse = authorApiClient.getAuthorWorks(normalizedKey);
            log.info("Found {} works for author: {}", worksResponse.getEntries().size(), authorKey);

            return worksResponse.getEntries().stream()
                    .map(entry -> WorkDTO.builder()
                            .workKey(entry.getKey())
                            .title(entry.getTitle())
                            .description(entry.getDescription())
                            .coverUrl(entry.getCoverId() != null ?
                                    "https://covers.openlibrary.org/b/id/" + entry.getCoverId() + "-M.jpg"
                                    : null)
                            .build())
                    .collect(Collectors.toList());

        } catch (FeignException.NotFound ex) {
            log.error("Author not found: {}", authorKey);
            throw new AuthorNotExists("Author with key " + authorKey + " not found." + ex.getMessage());
        } catch (FeignException ex) {
            log.error("Error fetching author works: {}", ex.getMessage());
            throw new AuthorNotExists("Failed to fetch author works: " + ex.contentUTF8() );
        } catch (Exception ex) {
            log.error("Unexpected error fetching author works: {}", ex.getMessage());
            throw new AuthorNotExists("Unexpected error: " + ex.getMessage());
        }
    }

    /**
     * Normalize author key format
     * e.g., "OL34221A" -> "OL34221A" or "/authors/OL34221A" -> "OL34221A"
     */
    private String normalizeAuthorKey(String authorKey) {
        if (authorKey == null || authorKey.isEmpty()) {
            return authorKey;
        }
        // Remove /authors/ prefix if present
        if (authorKey.startsWith("/authors/")) {
            return authorKey.substring("/authors/".length());
        }
        return authorKey;
    }
}
