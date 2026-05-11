package org.example.bookreadingapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookreadingapp.dto.auth.UserDto;
import org.example.bookreadingapp.entity.User;
import org.example.bookreadingapp.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public UserDto getCurrentUser() {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userRepository.findById(userId);
        log.info("User with email {} found: {}", userId, user.isPresent());
        return user.map(value -> UserDto.builder()
                .email(value.getEmail())
                .userName(value.getName())
                .build()).orElse(null);
    }
}
