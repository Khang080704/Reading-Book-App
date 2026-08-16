package org.example.bookreadingapp.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookreadingapp.client.AuthorApiClient;
import org.example.bookreadingapp.dto.author.*;
import org.example.bookreadingapp.dto.book.AuthorWorksDTO;
import org.example.bookreadingapp.dto.book.ProviderWorkPage;
import org.example.bookreadingapp.dto.book.WorkDTO;
import org.example.bookreadingapp.entity.AuthorDetail;
import org.example.bookreadingapp.entity.AuthorWorkSync;
import org.example.bookreadingapp.entity.Work;
import org.example.bookreadingapp.exception.definitions.AuthorNotExists;
import org.example.bookreadingapp.helper.AuthorHelper;
import org.example.bookreadingapp.repository.AuthorDetailRepository;
import org.example.bookreadingapp.repository.AuthorWorkSyncRepository;
import org.example.bookreadingapp.repository.WorkRepository;
import org.example.bookreadingapp.service.provider.SearchProvider;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorService {
    private final AuthorApiClient authorApiClient;
    private final AuthorDetailRepository authorDetailRepository;
    private final WorkRepository workRepository;
    private final AuthorWorkSyncRepository syncRepository;
    private final SearchProvider bookProvider;

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

            return AuthorDetailDTO.builder()
                    .bio(authorDetail.getBio())
                    .birthDate(authorDetail.getBirthDay())
                    .fullName(authorDetail.getFullName())
                    .createdAt(authorDetail.getCreatedAt())
                    .lastModifiedAt(authorDetail.getLastModify())
                    .avatar("https://covers.openlibrary.org/a/olid/" + olKey + "-M.jpg")
                    .build();

        }
        else {
            log.info("Author detail not found in database forolKey: {}", olKey);
            AuthorDetailResponse response = authorApiClient.getAuthorDetail(olKey);
            AuthorDetail detail = AuthorDetail.builder()
                    .bio(response.getBio())
                    .birthDay(response.getBirthDate())
                    .fullName(response.getFullName())
                    .olKey(normalizeAuthorKey(response.getKey()))
                    .createdAt(LocalDateTime.parse(response.getCreatedAt()))
                    .lastModify(LocalDateTime.parse(response.getLastModifiedAt()))
                    .build();
            log.info("Saving author information: {}", detail.toString());

            authorDetailRepository.save(detail);

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

    @Transactional
    public Page<WorkDTO> getWorksByAuthor(String authorKey, Pageable pageable) {
        AuthorDetail author = authorDetailRepository
                .findByOlKey(authorKey)
                .orElseThrow(() ->
                        new RuntimeException("Author not found")
                );

        AuthorWorkSync sync = syncRepository
                .findByAuthorDetail_OlKey(authorKey)
                .orElseGet(() ->
                        createInitialSync(author)
                );

        long requiredCount =
                (long) (pageable.getPageNumber() + 1)
                        * pageable.getPageSize();

        long localCount =
                workRepository.countByAuthors_OlKey(authorKey);

        int syncAttempts = 0;
        int maxSyncAttempts = 2;

        while (
                localCount < requiredCount
                        && sync.isHasNext()
                        && syncAttempts < maxSyncAttempts
        ) {
            syncNextBatch(author, sync);

            localCount =
                    workRepository.countByAuthors_OlKey(authorKey);

            syncAttempts++;
        }

        Page<Work> result = workRepository.findByAuthors_OlKey(
                authorKey,
                pageable
        );

        long total = sync.getTotalWork() != null
                ? sync.getTotalWork()
                : localCount;

        return mapWorkEntityToDto(result.getContent(), pageable, total);
    }

    private void syncNextBatch(
            AuthorDetail author,
            AuthorWorkSync sync
    ) {

        ProviderWorkPage response =
                bookProvider.getWorksByAuthor(
                        author,
                        sync.getNextOffset(),
                        sync.getBatchSize()
                );

        for (WorkDTO remote : response.works()) {

            Work work = workRepository
                    .findByWorkKey(remote.getWorkKey())
                    .orElseGet(() ->
                            Work.builder()
                                    .workKey(remote.getWorkKey())
                                    .title(remote.getTitle())
                                    .description(remote.getDescription())
                                    .coverId(remote.getCoverUrl())
                                    .build()
                    );

            work.getAuthors().add(author);

            workRepository.save(work);
        }

        sync.setNextOffset(
                sync.getNextOffset()
                        + response.works().size()
        );

        sync.setHasNext(response.hasNext());
        sync.setLastSyncAt(Instant.now());
        sync.setTotalWork(response.totalElement());

        syncRepository.save(sync);
    }

    private AuthorWorkSync createInitialSync(
            AuthorDetail author
    ) {
        return AuthorWorkSync.builder()
                .authorDetail(author)
                .nextOffset(0)
                .batchSize(50)
                .totalWork(null)
                .hasNext(true)
                .build();
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

    private Page<WorkDTO> mapWorkEntityToDto (List<Work> data, Pageable pageable, long total) {
        if(data == null || data.isEmpty()) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        List<WorkDTO> result = data.stream().map(work -> WorkDTO.builder()
                .workKey(work.getWorkKey())
                .title(work.getTitle())
                .description(work.getDescription())
                .coverUrl(work.getCoverId())
                .build()
        ).toList();

        return new PageImpl<>(result, pageable, total);
    }
}
