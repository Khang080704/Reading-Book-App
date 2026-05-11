package org.example.bookreadingapp.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookreadingapp.client.AuthorApiClient;
import org.example.bookreadingapp.dto.author.*;
import org.example.bookreadingapp.dto.book.AuthorWorksDTO;
import org.example.bookreadingapp.dto.book.WorkDTO;
import org.example.bookreadingapp.entity.AuthorDetail;
import org.example.bookreadingapp.exception.definitions.AuthorNotExists;
import org.example.bookreadingapp.helper.AuthorHelper;
import org.example.bookreadingapp.repository.AuthorDetailRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorService {
    private final AuthorApiClient authorApiClient;
    private final AuthorDetailRepository authorDetailRepository;
    private final AuthorHelper authorHelper;

    @Cacheable(value = "author", key = "#authorName + '-' + #limit")
    public List<AuthorDTO> searchAuthors(String authorName, int limit) {
        log.info("Cache miss for searchAuthors with authorName: {}, page: {}, limit: {}", authorName, limit);

        AuthorListResponse response = authorApiClient.getAuthors(authorName, limit);
        return response.getDocs().stream().map(doc -> {
            AuthorDTO authorDTO = AuthorDTO.builder()
                    .id(doc.getKey())
                    .name(doc.getName())
                    .birthDay(doc.getBirthDay())
                    .olKey(doc.getKey())
                    .avatar("https://covers.openlibrary.org/a/olid/" + doc.getKey() + "-M.jpg")
                    .readCount(doc.getReadingCount())
                    .build();
            return authorDTO;
        }).collect(Collectors.toList());
    }

    public AuthorDetailDTO getAuthorDetail(String olKey) throws AuthorNotExists {
        Optional<AuthorDetail> author = authorDetailRepository.findByOlKey(olKey);

        if(author.isPresent()) {
            AuthorDetail authorDetail = author.get();
            AuthorDetailDTO dto = new AuthorDetailDTO();
            dto.setBio(authorDetail.getBio());
            dto.setBirthDate(authorDetail.getBirthDay());
            dto.setFullName(authorDetail.getFullName());
            dto.setCreatedAt(authorDetail.getCreatedAt());
            dto.setLastModifiedAt(authorDetail.getLastModify());
            return dto;
        }
        else {
            log.info("Author detail not found in database forolKey: {}", olKey);
            AuthorDetailResponse response = authorApiClient.getAuthorDetail(olKey);
            AuthorDetail detail = AuthorDetail.builder()
                    .bio(response.getBio())
                    .birthDay(response.getBirthDate())
                    .fullName(response.getFullName())
                    .olKey(response.getKey())
                    .createdAt(LocalDateTime.parse(response.getCreatedAt()))
                    .lastModify(LocalDateTime.parse(response.getLastModifiedAt()))
                    .build();

            authorHelper.saveAuthorDetail(detail);

            return AuthorDetailDTO.builder()
                    .birthDate(detail.getBirthDay())
                    .fullName(detail.getFullName())
                    .bio(detail.getBio())
                    .createdAt(detail.getCreatedAt())
                    .lastModifiedAt(detail.getLastModify())
                    .avatar("https://covers.openlibrary.org/a/olid/" + olKey + "-M.jpg")
                    .build();
        }
    }

    /**
     * Get works by author from Open Library
     * Uses Author Works API: /authors/{authorKey}/works.json
     * https://openlibrary.org/dev/docs/api/authors
     */
    @Cacheable(value = "authorWorks", key = "#authorKey")
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
