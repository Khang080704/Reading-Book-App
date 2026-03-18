package org.example.bookreadingapp.service;

import org.example.bookreadingapp.dto.TokenResponse;
import org.example.bookreadingapp.dto.UserDto;
import org.example.bookreadingapp.entity.User;
import org.example.bookreadingapp.exception.definitions.EmailExists;
import org.example.bookreadingapp.exception.definitions.WrongCredentials;
import org.example.bookreadingapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto register(String email, String password, String userName) throws EmailExists {
        Optional<User> userExisting = userRepository.findByEmail(email);
        if(userExisting.isPresent()){
            throw new EmailExists("Email already exists");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        userDto.setUserName(userName);

        return userDto;
    }

    public TokenResponse login(String email, String password) throws WrongCredentials {
        User existingUser = userRepository.findByEmail(email).orElseThrow(
                () -> new WrongCredentials("email or password are wrong")
        );

        if (!passwordEncoder.matches(password, existingUser.getPassword())) {
            throw new WrongCredentials("email or password are wrong");
        }
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken(jwtService.generateAccessToken(existingUser.getId()));
        tokenResponse.setRefreshToken(jwtService.generateRefreshToken(existingUser.getId()));
        return tokenResponse;
    }
}
