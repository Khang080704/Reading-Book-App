package org.example.bookreadingapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotNull
    private String username;

    @NotNull
    @Min(value = 6, message = "password must be at least 6 characters")
    private String password;

    @NotNull
    @Email
    private String email;
}
