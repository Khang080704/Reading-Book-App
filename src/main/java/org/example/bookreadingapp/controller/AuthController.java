package org.example.bookreadingapp.controller;

import jakarta.validation.Valid;
import org.example.bookreadingapp.dto.LoginRequest;
import org.example.bookreadingapp.dto.RegisterRequest;
import org.example.bookreadingapp.dto.TokenResponse;
import org.example.bookreadingapp.dto.UserDto;
import org.example.bookreadingapp.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody RegisterRequest request) {
        UserDto userDto1 = authService.register(request.getEmail(), request.getPassword(), request.getUsername());
        return ResponseEntity.ok(userDto1);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse result = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(result);
    }



}
