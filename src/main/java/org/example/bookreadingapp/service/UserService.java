package org.example.bookreadingapp.service;

import lombok.RequiredArgsConstructor;
import org.example.bookreadingapp.dto.UserDto;
import org.example.bookreadingapp.entity.User;
import org.example.bookreadingapp.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto getCurrentUser() {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userRepository.findByEmail(userId);
        return user.map(value -> UserDto.builder()
                .email(value.getEmail())
                .userName(value.getName())
                .build()).orElse(null);
    }
}
