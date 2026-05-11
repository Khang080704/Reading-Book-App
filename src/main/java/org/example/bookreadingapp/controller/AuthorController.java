package org.example.bookreadingapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookreadingapp.dto.author.AuthorDTO;
import org.example.bookreadingapp.dto.author.AuthorDetailDTO;
import org.example.bookreadingapp.dto.book.WorkDTO;
import org.example.bookreadingapp.service.AuthorService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/author")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping("/search")
    public ResponseEntity<List<AuthorDTO>> searchAuthors(
            @RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ){
        List<AuthorDTO> authors = authorService.searchAuthors(q, limit);
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/{olkey}")
    public ResponseEntity<AuthorDetailDTO> getAuthorDetail(@PathVariable String olkey) {
        AuthorDetailDTO authorDetail = authorService.getAuthorDetail(olkey);
        return ResponseEntity.ok(authorDetail);
    }

    /**
     * Get all works by an author
     * Works are retrieved from Open Library Author Works API
     * https://openlibrary.org/dev/docs/api/authors (Works by an Author section)
     *
     * @param authorKey OpenLibrary author key (e.g., OL34221A)
     * @return list of works by the author
     */
    @GetMapping("/{authorKey}/works")
    public ResponseEntity<List<WorkDTO>> getAuthorWorks(@PathVariable String authorKey) {
        List<WorkDTO> works = authorService.getAuthorWorks(authorKey);
        return ResponseEntity.ok(works);
    }
}
