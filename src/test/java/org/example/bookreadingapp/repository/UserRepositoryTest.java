package org.example.bookreadingapp.repository;

import org.example.bookreadingapp.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    public void testExistUser_shouldReturnTrue_WhenUserExists() {
        User user = new User();
        user.setEmail("pgkhangt1@gmail.com");
        user.setPassword(passwordEncoder.encode("123456"));
        user.setName("Pham Gia khang");
        userRepository.save(user);

        boolean isUserExists = userRepository.findByEmail("pgkhangt1@gmail.com").isPresent();
        assertTrue(isUserExists);
    }

    @Test
    public void testExistUser_shouldReturnFalse_WhenUserDoesNotExist() {
        boolean isUserExists = userRepository.findByEmail("test@gmail.com").isPresent();
        assertFalse(isUserExists);
    }

    @Test
    public void testEmailExist_shouldReturnTrue() {
        User user = new User();
        user.setEmail("pgkhangt1@gmail.com");
        user.setPassword(passwordEncoder.encode("123456"));
        user.setName("Pham Gia khang");
        userRepository.save(user);

        User user2 = userRepository.findByEmail("pgkhangt1@gmail.com").orElseThrow();
        assertEquals(user2.getEmail(), user.getEmail());
        assertEquals(user2.getName(), user.getName());
    }


}
