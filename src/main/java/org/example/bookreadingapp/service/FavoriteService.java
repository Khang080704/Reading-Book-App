package org.example.bookreadingapp.service;

import lombok.RequiredArgsConstructor;
import org.example.bookreadingapp.dto.author.AuthorDetailDTO;
import org.example.bookreadingapp.dto.book.BookDetailDTO;
import org.example.bookreadingapp.entity.AuthorDetail;
import org.example.bookreadingapp.entity.User;
import org.example.bookreadingapp.entity.Work;
import org.example.bookreadingapp.repository.AuthorDetailRepository;
import org.example.bookreadingapp.repository.UserRepository;
import org.example.bookreadingapp.repository.WorkRepository;
import org.example.bookreadingapp.dto.book.WorkDTO;
import org.example.bookreadingapp.dto.author.AuthorDTO;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final UserRepository userRepository;
    private final WorkRepository workRepository;
    private final AuthorDetailRepository authorRepository;
    private final AuthorService authorService;
    private final SearchService searchService;

    @Transactional
    public void addFavoriteWork(String userId, String workKey) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Work work = workRepository.findByWorkKey(workKey)
                .orElseGet(() -> {
                    BookDetailDTO dto = searchService.getWorkDetails(workKey);
                    return Work.builder()
                            .title(dto.getTitle())
                            .coverId(dto.getCoverUrl())
                            .workKey(workKey)
                            .description(dto.getDescription())
                            .build();
                });

        user.getFavoriteWorks().add(work);
        userRepository.save(user);
    }

    @Transactional
    public void addFavoriteAuthor(String userId, String authorKey) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        AuthorDetail author = authorRepository.findByOlKey(authorKey)
                .orElseGet(() -> {
                    AuthorDetailDTO dto = authorService.getAuthorDetail(authorKey);
                    return AuthorDetail.builder()
                            .bio(dto.getBio())
                            .birthDay(dto.getBirthDate())
                            .fullName(dto.getFullName())
                            .olKey(authorKey)
                            .createdAt(dto.getCreatedAt())
                            .lastModify(dto.getLastModifiedAt())
                            .build();
                });

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

    @Transactional(readOnly = true)
    public List<WorkDTO> getFavoriteWorks(String userId) {
        Optional<User> userOpt = userRepository.findWithFavoriteAuthorsById(userId);
        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        User user = userOpt.get();
        return user.getFavoriteWorks()
                .stream().map(w -> WorkDTO.builder()
                        .workKey(w.getWorkKey())
                        .title(w.getTitle())
                        .coverUrl(w.getCoverId())
                        .description(w.getDescription())
                        .build()).toList();
    }

    @Transactional(readOnly = true)
    public List<AuthorDTO> getFavoriteAuthors(String userId) {
        Optional<User> userOpt = userRepository.findWithFavoriteAuthorsById(userId);
        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        User user = userOpt.get();

        return user.getFavoriteAuthors()
                .stream().map(a -> AuthorDTO.builder()
                        .name(a.getFullName())
                        .birthDay(a.getBirthDay())
                        .olKey(a.getOlKey())
                        .avatar("https://covers.openlibrary.org/a/olid/" + a.getOlKey() + "-M.jpg")
                        .build()).toList();
    }
}
