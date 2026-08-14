package org.example.bookreadingapp.service.provider;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookreadingapp.client.BookApiClient;
import org.example.bookreadingapp.dto.book.SearchBookDTO;
import org.example.bookreadingapp.dto.book.SearchBooksDTO;
import org.springframework.context.annotation.Primary;
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
}
