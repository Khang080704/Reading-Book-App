package org.example.bookreadingapp.dto;

import lombok.Data;

@Data
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
}
