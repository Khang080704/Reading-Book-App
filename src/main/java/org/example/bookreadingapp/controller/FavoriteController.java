package org.example.bookreadingapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookreadingapp.dto.author.AuthorDetailDTO;
import org.example.bookreadingapp.entity.User;
import org.example.bookreadingapp.repository.UserRepository;
import org.example.bookreadingapp.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.example.bookreadingapp.dto.book.WorkDTO;
import org.example.bookreadingapp.dto.author.AuthorDTO;
import java.util.List;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final UserRepository userRepository;

    private String getCurrentUserId(Jwt jwt) {
        String keycloakUserId = jwt.getSubject();
        User user = userRepository.findByKeycloakId(keycloakUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }

    @GetMapping("/works")
    public ResponseEntity<List<WorkDTO>> getFavoriteWorks(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(favoriteService.getFavoriteWorks(getCurrentUserId(jwt)));
    }

    @GetMapping("/authors")
    public ResponseEntity<List<AuthorDTO>> getFavoriteAuthors(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(favoriteService.getFavoriteAuthors(getCurrentUserId(jwt)));
    }

    @PostMapping("/works/{workKey}")
    public ResponseEntity<String> addFavoriteWork(@PathVariable String workKey, @AuthenticationPrincipal Jwt jwt) {
        favoriteService.addFavoriteWork(getCurrentUserId(jwt), workKey);
        return ResponseEntity.ok("Đã thêm tác phẩm vào danh sách yêu thích");
    }

    @DeleteMapping("/works/{workKey}")
    public ResponseEntity<String> removeFavoriteWork(@PathVariable String workKey, @AuthenticationPrincipal Jwt jwt) {
        favoriteService.removeFavoriteWork(getCurrentUserId(jwt), workKey);
        return ResponseEntity.ok("Đã xóa tác phẩm khỏi danh sách yêu thích");
    }

    @PostMapping("/authors/{authorKey}")
    public ResponseEntity<String> addFavoriteAuthor(@PathVariable String authorKey, @AuthenticationPrincipal Jwt jwt) {
        favoriteService.addFavoriteAuthor(getCurrentUserId(jwt), authorKey);
        return ResponseEntity.ok("Đã thêm tác giả vào danh sách yêu thích");
    }

    @DeleteMapping("/authors/{authorKey}")
    public ResponseEntity<String> removeFavoriteAuthor(@PathVariable String authorKey, @AuthenticationPrincipal Jwt jwt) {
        favoriteService.removeFavoriteAuthor(getCurrentUserId(jwt), authorKey);
        return ResponseEntity.ok("Đã xóa tác giả khỏi danh sách yêu thích");
    }

    @GetMapping("/works/{workKey}/status")
    public ResponseEntity<Boolean> isWorkFavorite(@PathVariable String workKey, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(favoriteService.isWorkFavorite(getCurrentUserId(jwt), workKey));
    }

    @GetMapping("/authors/{authorKey}/status")
    public ResponseEntity<Boolean> isAuthorFavorite(@PathVariable String authorKey, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(favoriteService.isAuthorFavorite(getCurrentUserId(jwt), authorKey));
    }
}
