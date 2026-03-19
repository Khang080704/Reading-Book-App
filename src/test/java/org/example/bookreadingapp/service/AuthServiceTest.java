package org.example.bookreadingapp.service;

import lombok.extern.slf4j.Slf4j;
import org.example.bookreadingapp.dto.TokenResponse;
import org.example.bookreadingapp.dto.UserDto;
import org.example.bookreadingapp.entity.User;
import org.example.bookreadingapp.exception.definitions.EmailExists;
import org.example.bookreadingapp.exception.definitions.WrongCredentials;
import org.example.bookreadingapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private AuthService authService;

    @Test
    public void register_should_save_user() {

        when(userRepository.findByEmail("email@gmail.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");


        UserDto dto = authService.register("email@gmail.com", "password", "username");

        verify(userRepository, times(1)).save(any(User.class));
        assertEquals( "email@gmail.com", dto.getEmail());
        assertEquals("username", dto.getUserName());
    }

    @Test
    public void register_should_return_false_when_email_exist() {
        User existingUser = mockUser();

        when(userRepository.findByEmail("email@gmail.com")).thenReturn(Optional.of(existingUser));

        assertThrows(EmailExists.class, () -> authService.register(existingUser.getEmail(), "password", "username"));
    }

    @Test
    public void login_should_save_user() {
        String email = "email@gmail.com";
        String password = "password";
        User user = mockUser();
        user.setEmail(email);
        user.setPassword(password);


        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);



        when(jwtService.generateAccessToken(anyString())).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(anyString())).thenReturn("refreshToken");

        TokenResponse tokenResponse = authService.login(email, password);

        assertEquals( "accessToken", tokenResponse.getAccessToken());
        assertEquals( "refreshToken", tokenResponse.getRefreshToken());

    }

    @Test
    public void login_should_fail_because_email_is_incorrect() {
        String email = "email@gmail.com";
        String password = "password";
        User user = mockUser();
        user.setEmail(email);
        user.setPassword(password);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThrows(WrongCredentials.class, () -> authService.login(email, password));
        verify(jwtService, times(0)).generateAccessToken(anyString());
        verify(jwtService, times(0)).generateRefreshToken(anyString());
    }

    @Test
    public void login_should_fail_because_password_is_incorrect() {
        String email = "email@gmail.com";
        String password = "password";
        User user = mockUser();
        user.setEmail(email);
        user.setPassword(password);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);
        assertThrows(WrongCredentials.class, () -> authService.login(email, password));

        verify(jwtService, times(0)).generateAccessToken(anyString());
        verify(jwtService, times(0)).generateRefreshToken(anyString());
    }

    private User mockUser() {
        User user = new User();
        user.setId("123");
        user.setName("username");
        user.setPassword("password");
        user.setEmail("email@gmail.com");
        return user;
    }
}
