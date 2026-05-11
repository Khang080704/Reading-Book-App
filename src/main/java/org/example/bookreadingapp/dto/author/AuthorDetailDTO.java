package org.example.bookreadingapp.dto.author;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorDetailDTO {
    private String birthDate;

    private String fullName;

    private String bio;

    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;

    private String avatar;
}
