package org.example.bookreadingapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookreadingapp.dto.author.AuthorDetailDTO;
import org.example.bookreadingapp.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.example.bookreadingapp.dto.book.WorkDTO;
import org.example.bookreadingapp.dto.author.AuthorDTO;
import java.util.List;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    private String getCurrentUserId() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping("/works")
    public ResponseEntity<List<WorkDTO>> getFavoriteWorks() {
        return ResponseEntity.ok(favoriteService.getFavoriteWorks(getCurrentUserId()));
    }

    @GetMapping("/authors")
    public ResponseEntity<List<AuthorDTO>> getFavoriteAuthors() {
        return ResponseEntity.ok(favoriteService.getFavoriteAuthors(getCurrentUserId()));
    }

    @PostMapping("/works/{workKey}")
    public ResponseEntity<String> addFavoriteWork(@PathVariable String workKey) {
        favoriteService.addFavoriteWork(getCurrentUserId(), workKey);
        return ResponseEntity.ok("Đã thêm tác phẩm vào danh sách yêu thích");
    }

    @DeleteMapping("/works/{workKey}")
    public ResponseEntity<String> removeFavoriteWork(@PathVariable String workKey) {
        favoriteService.removeFavoriteWork(getCurrentUserId(), workKey);
        return ResponseEntity.ok("Đã xóa tác phẩm khỏi danh sách yêu thích");
    }

    @PostMapping("/authors/{authorKey}")
    public ResponseEntity<String> addFavoriteAuthor(@PathVariable String authorKey) {
        favoriteService.addFavoriteAuthor(getCurrentUserId(), authorKey);
        return ResponseEntity.ok("Đã thêm tác giả vào danh sách yêu thích");
    }

    @DeleteMapping("/authors/{authorKey}")
    public ResponseEntity<String> removeFavoriteAuthor(@PathVariable String authorKey) {
        favoriteService.removeFavoriteAuthor(getCurrentUserId(), authorKey);
        return ResponseEntity.ok("Đã xóa tác giả khỏi danh sách yêu thích");
    }
}
