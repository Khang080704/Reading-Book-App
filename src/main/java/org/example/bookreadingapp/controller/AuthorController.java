package org.example.bookreadingapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookreadingapp.dto.AuthorDTO;
import org.example.bookreadingapp.dto.AuthorDetailDTO;
import org.example.bookreadingapp.entity.Author;
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
    public ResponseEntity<List<AuthorDTO>> searchAuthors(@RequestParam(defaultValue = "") String q){
        List<AuthorDTO> authors = authorService.searchAuthors(q);
        return ResponseEntity.ok(authors);
    }

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAuthors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "desc") String direction){
        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(page, size, sort);
        List<AuthorDTO> authors = authorService.getAuthors(pageRequest);
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/{olkey}")
    public ResponseEntity<AuthorDetailDTO> getAuthorDetail(@PathVariable String olkey) {
        AuthorDetailDTO authorDetail = authorService.getAuthorDetail(olkey);
        return ResponseEntity.ok(authorDetail);
    }
}
