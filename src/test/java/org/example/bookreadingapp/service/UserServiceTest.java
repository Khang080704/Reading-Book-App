package org.example.bookreadingapp.service;

import org.example.bookreadingapp.dto.auth.UserDto;
import org.example.bookreadingapp.entity.User;
import org.example.bookreadingapp.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    // @Test
    // public void should_return_user() {
    //     User mockUser = new User();
    //     mockUser.setEmail("email@gmail.com");
    //     mockUser.setName("Nguyen Van A");


    //     when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(mockUser);


    //     UserDto result = userService.getCurrentUser();

    //     assertEquals("email@gmail.com", result.getEmail());
    //     assertEquals("Nguyen Van A", result.getUserName());

    //     verify(userRepository, times(1)).findByEmail("email@gmail.com");


    // }


}
