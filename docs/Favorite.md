Để triển khai tính năng "Yêu thích" (Favorites), chúng ta cần thiết lập mối quan hệ **Nhiều - Nhiều (ManyToMany)** giữa bảng người dùng (`User`) với các bảng `Work` và `AuthorDetail`.

Đặc biệt, ở tầng Service, chúng ta sẽ áp dụng chiến lược **"Tạo Stub"**: Nếu người dùng yêu thích một Tác phẩm/Tác giả chưa từng tồn tại trong Database của bạn, hệ thống sẽ tự động tạo một bản ghi Stub để lưu trữ quan hệ ngay lập tức.

Dưới đây là kiến trúc và mã nguồn hoàn chỉnh cho tính năng này:

### 1. Cập nhật Entity (Bổ sung bảng User)

Nếu bạn chưa có bảng `User`, hãy tạo một Entity đơn giản. Nếu đã có, bạn chỉ cần bổ sung 2 thuộc tính `favoriteWorks` và `favoriteAuthors`.

```java
package org.example.bookreadingapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String username;
    private String email;

    // Quan hệ User - Favorite Works
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_favorite_works",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "work_id")
    )
    @Builder.Default
    private Set<Work> favoriteWorks = new HashSet<>();

    // Quan hệ User - Favorite Authors
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_favorite_authors",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @Builder.Default
    private Set<AuthorDetail> favoriteAuthors = new HashSet<>();
}

```

---

### 2. Tầng Service (Xử lý Logic & Tạo Stub)

Service này sẽ chịu trách nhiệm thêm/xóa dữ liệu yêu thích. Lưu ý cách hàm `orElseGet()` hoạt động để tự động tạo Stub nếu không tìm thấy dữ liệu trong kho.

```java
package org.example.bookreadingapp.service;

import lombok.RequiredArgsConstructor;
import org.example.bookreadingapp.entity.AuthorDetail;
import org.example.bookreadingapp.entity.User;
import org.example.bookreadingapp.entity.Work;
import org.example.bookreadingapp.repository.AuthorDetailRepository;
import org.example.bookreadingapp.repository.UserRepository;
import org.example.bookreadingapp.repository.WorkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final UserRepository userRepository;
    private final WorkRepository workRepository;
    private final AuthorDetailRepository authorRepository;

    @Transactional
    public void addFavoriteWork(String userId, String workKey) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Tìm Work, nếu chưa có trong DB thì tạo Stub
        Work work = workRepository.findByWorkKey(workKey)
                .orElseGet(() -> workRepository.save(Work.builder().workKey(workKey).build()));

        user.getFavoriteWorks().add(work);
        userRepository.save(user);
    }

    @Transactional
    public void addFavoriteAuthor(String userId, String authorKey) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Tìm Author, nếu chưa có trong DB thì tạo Stub
        AuthorDetail author = authorRepository.findByOlKey(authorKey)
                .orElseGet(() -> authorRepository.save(AuthorDetail.builder().olKey(authorKey).build()));

        user.getFavoriteAuthors().add(author);
        userRepository.save(user);
    }

    @Transactional
    public void removeFavoriteWork(String userId, String workKey) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        workRepository.findByWorkKey(workKey)
                .ifPresent(work -> {
                    user.getFavoriteWorks().remove(work);
                    userRepository.save(user);
                });
    }

    @Transactional
    public void removeFavoriteAuthor(String userId, String authorKey) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        authorRepository.findByOlKey(authorKey)
                .ifPresent(author -> {
                    user.getFavoriteAuthors().remove(author);
                    userRepository.save(user);
                });
    }
}

```

---

### 3. Tầng Controller (Cung cấp Endpoints)

Thiết kế API theo chuẩn RESTful. Thực tế, `userId` thường được lấy từ token (ví dụ: Spring Security JWT `Principal` hoặc `@AuthenticationPrincipal`), nhưng để làm mẫu, tôi sẽ lấy qua tham số.

```java
package org.example.bookreadingapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookreadingapp.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    // Giả lập lấy ID của user đang đăng nhập. (Trong thực tế dùng SecurityContext)
    private final String CURRENT_USER_ID = "user-uuid-1234";

    @PostMapping("/works/{workKey}")
    public ResponseEntity<String> addFavoriteWork(@PathVariable String workKey) {
        favoriteService.addFavoriteWork(CURRENT_USER_ID, workKey);
        return ResponseEntity.ok("Đã thêm tác phẩm vào danh sách yêu thích");
    }

    @DeleteMapping("/works/{workKey}")
    public ResponseEntity<String> removeFavoriteWork(@PathVariable String workKey) {
        favoriteService.removeFavoriteWork(CURRENT_USER_ID, workKey);
        return ResponseEntity.ok("Đã xóa tác phẩm khỏi danh sách yêu thích");
    }

    @PostMapping("/authors/{authorKey}")
    public ResponseEntity<String> addFavoriteAuthor(@PathVariable String authorKey) {
        favoriteService.addFavoriteAuthor(CURRENT_USER_ID, authorKey);
        return ResponseEntity.ok("Đã thêm tác giả vào danh sách yêu thích");
    }

    @DeleteMapping("/authors/{authorKey}")
    public ResponseEntity<String> removeFavoriteAuthor(@PathVariable String authorKey) {
        favoriteService.removeFavoriteAuthor(CURRENT_USER_ID, authorKey);
        return ResponseEntity.ok("Đã xóa tác giả khỏi danh sách yêu thích");
    }
}

```



**Mẹo tối ưu:**
Khi query danh sách Favorites trả về cho Client, bạn đừng trả nguyên đối tượng `User` kèm mảng Entity gốc (vì có thể dính lỗi Lazy Initialization hoặc tốn quá nhiều băng thông). Thay vào đó, hãy viết một JPQL Query riêng trong Repository để lấy trực tiếp `List<WorkDto>` hoặc `List<AuthorDto>` thuộc về `user_id` đó.