package org.example.bookreadingapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookreadingapp.dto.book.BookDetailDTO;
import org.example.bookreadingapp.dto.book.EditionDTO;
import org.example.bookreadingapp.dto.book.EditionsListDTO;
import org.example.bookreadingapp.dto.book.SearchBookDTO;
import org.example.bookreadingapp.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Book Controller - handles book search functionality
 * Note: Work details are retrieved through Author endpoints
 * See: GET /api/v1/author/{authorKey}/works
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
public class BookController {
    private final SearchService searchService;

    /**
     * Search books by title, author, or other criteria
     * Uses Open Library Search API
     * https://openlibrary.org/dev/docs/api/search
     *
     * @param q search query (e.g., "harry potter", "tolkien")
     * @param page page number (default 1)
     * @param limit results per page (default 10)
     * @return list of books matching the search query
     */
    @GetMapping("/search")
    public ResponseEntity<List<SearchBookDTO>> searchBooks(
            @RequestParam String q,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        List<SearchBookDTO> results = searchService.searchBooks(q, page, limit);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/works/{workKey}")
    public ResponseEntity<BookDetailDTO> getWorkDetails(@PathVariable String workKey) {
        return ResponseEntity.ok(searchService.getWorkDetails(workKey));
    }

    @GetMapping("/works/{workKey}/editions")
    public ResponseEntity<EditionsListDTO> getWorkEditions(@PathVariable String workKey) {
        return ResponseEntity.ok(searchService.getWorkEditions(workKey));
    }

    @GetMapping("/editions/{editionKey}")
    public ResponseEntity<EditionDTO> getEditionDetails(@PathVariable String editionKey) {
        return ResponseEntity.ok(searchService.getEditionDetails(editionKey));
    }
}
