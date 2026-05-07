package org.example.bookreadingapp.dto.author;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuthorDetailDTO {
    private String birthDate;

    private String fullName;

    private String bio;

    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;
}
