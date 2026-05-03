package org.example.bookreadingapp.seed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookreadingapp.entity.User;
import org.example.bookreadingapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserSeed implements CommandLineRunner {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Seed begin");

        Optional<User> authUserOptional = userRepository.findByEmail("example@gmail.com");
        if(authUserOptional.isPresent()){
            log.info("User already exists, seed end");
            return;
        }

        User user = new User();
        user.setName("Nguyen Van A");
        user.setEmail("example@gmail.com");
        user.setPassword(passwordEncoder.encode("123456"));

        userRepository.save(user);

        log.info("Seed end");
    }
}
