package org.example.bookreadingapp.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    private final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 30;  // 1 hour
    private final long REFRESH_TOKEN_EXPIRATION = 1000L * 60 * 60 * 24 * 7; // 7 days

    private Key getSignKey() {
        String jwtSecretKey = "my-secret-key-is-a-key-that-is-very-long-and-it-should-be-longer-than-32-character";
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes());
    }

    private String createToken(Map<String, Object> claims, String userId, long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateAccessToken(String userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        return createToken(map, userId, ACCESS_TOKEN_EXPIRATION);
    }
    public String generateRefreshToken(String userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        return createToken(map, userId, REFRESH_TOKEN_EXPIRATION);
    }

    public String extractUsername(String token) {
        return parseToken(token).getSubject();
    }

    public String extractUserId(String token) {
        Object id = parseToken(token).get("user_id");
        if (id instanceof String i) return id.toString();
        return null;
    }

    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
    private boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }
    private Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
