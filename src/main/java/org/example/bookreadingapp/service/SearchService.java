package org.example.bookreadingapp.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookreadingapp.client.BookApiClient;
import org.example.bookreadingapp.dto.book.SearchBooksDTO;
import org.example.bookreadingapp.dto.book.SearchBookDTO;
import org.example.bookreadingapp.entity.Book;
import org.example.bookreadingapp.exception.definitions.BookApiException;
import org.example.bookreadingapp.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {
    private final BookApiClient bookApiClient;
    private final BookRepository bookRepository;

    /**
     * Search books by query
     * Uses Open Library Search API
     * https://openlibrary.org/dev/docs/api/search
     *
     * @param query search query (e.g., "harry potter", "tolkien")
     * @param page page number (default 1)
     * @param limit results per page (default 10)
     * @return list of books matching the search query
     */
    public List<SearchBookDTO> searchBooks(String query, int page, int limit) {
        log.info("Searching books for query: {} (page: {}, limit: {})", query, page, limit);

        try {
            int dbPage = Math.max(page - 1, 0);
            int apiPage = Math.max(page, 1);

            Pageable pageable = PageRequest.of(dbPage, limit);
            Page<Book> result = bookRepository.findByTitleContainingIgnoreCase(query, pageable);
            log.info("Found {} books in local database for query: {}", result.getTotalElements(), query);

            if (result.hasContent()) {
                return mapBooksToSearchDTO(result.getContent());
            }

            SearchBooksDTO apiResponse = bookApiClient.searchBooks(query, apiPage, limit);
            log.info("Found {} books from Open Library", apiResponse.getNumFound());

            List<SearchBooksDTO.BookSearchEntry> docs = apiResponse.getDocs() == null ? List.of() : apiResponse.getDocs();
            List<Book> booksToSave = docs.stream()
                    .map(this::toBookEntity)
                    .filter(book -> book.getBookKey() != null)
                    .map(this::mergeWithExistingBook)
                    .collect(Collectors.toCollection(ArrayList::new));

            if (!booksToSave.isEmpty()) {
                bookRepository.saveAll(booksToSave);
            }

            return mapEntriesToSearchDTO(docs);

        } catch (FeignException.NotFound ex) {
            log.warn("No books found for query: {}", query);
            throw new BookApiException("No books found for query: " + query, 404, ex);
        } catch (FeignException ex) {
            log.error("Error calling Open Library Search API: {}", ex.getMessage());
            throw new BookApiException("Failed to search books: " + ex.contentUTF8(), ex.status(), ex);
        } catch (Exception ex) {
            log.error("Unexpected error searching books: {}", ex.getMessage());
            throw new BookApiException("Unexpected error when searching: " + ex.getMessage(), 500, ex);
        }
    }

    private Book toBookEntity(SearchBooksDTO.BookSearchEntry entry) {
        return Book.builder()
                .bookKey(entry.getKey())
                .title(entry.getTitle())
                .firstPublishYear(entry.getFirstPublishYear())
                .isbn(entry.getFirstIsbn())
                .editionCount(entry.getEditionCount())
                .build();
    }

    private Book mergeWithExistingBook(Book book) {
        Optional<Book> existingBook = bookRepository.findByBookKey(book.getBookKey());
        if (existingBook.isEmpty()) {
            return book;
        }

        Book current = existingBook.get();
        current.setTitle(book.getTitle());
        current.setFirstPublishYear(book.getFirstPublishYear());
        current.setIsbn(book.getIsbn());
        current.setEditionCount(book.getEditionCount());
        current.setAuthor(book.getAuthor());
        return current;
    }

    private List<SearchBookDTO> mapBooksToSearchDTO(List<Book> books) {
        return books.stream()
                .map(book -> SearchBookDTO.builder()
                        .bookKey(book.getBookKey())
                        .title(book.getTitle())
                        .authorNames(book.getAuthor() != null ? new String[]{book.getAuthor().getName()} : new String[0])
                        .firstPublishYear(book.getFirstPublishYear())
                        .isbn(book.getIsbn())
                        .editionCount(book.getEditionCount())
                        .coverUrl(null)
                        .build())
                .collect(Collectors.toList());
    }

    private List<SearchBookDTO> mapEntriesToSearchDTO(List<SearchBooksDTO.BookSearchEntry> docs) {
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
