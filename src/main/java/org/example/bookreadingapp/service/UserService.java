package org.example.bookreadingapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookreadingapp.dto.auth.UserDto;
import org.example.bookreadingapp.entity.User;
import org.example.bookreadingapp.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public UserDto getCurrentUser(Jwt jwt) {
        String keycloakUserId = jwt.getSubject();

        User user = userRepository
                .findByKeycloakId(keycloakUserId)
                .orElseGet(() -> createUser(jwt));

        return UserDto.builder()
                .email(user.getEmail())
                .userName(user.getName())
                .build();

    }

    private User createUser(Jwt jwt) {
        User user = User.builder()
                .keycloakId(jwt.getSubject())
                .email(jwt.getClaimAsString("email"))
                .name(jwt.getClaimAsString("preferred_username"))
                .build();
        return userRepository.save(user);
    }
}
