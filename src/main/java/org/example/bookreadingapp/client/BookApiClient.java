package org.example.bookreadingapp.client;

import org.example.bookreadingapp.dto.book.SearchBooksDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Book API Client for searching books
 * API: https://openlibrary.org/dev/docs/api/search
 * Example: /search.json?q=harry+potter&page=1
 */
@FeignClient(name = "bookApiClient", url = "https://openlibrary.org")
public interface BookApiClient {
    /**
     * Search books by title, author, or other criteria
     * 
     * @param q search query (e.g., "harry potter")
     * @param page page number (starts from 1)
     * @param limit results per page
     * @return search results containing matching books
     */
    @GetMapping("/search.json")
    SearchBooksDTO searchBooks(
            @RequestParam String q,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    );
}
