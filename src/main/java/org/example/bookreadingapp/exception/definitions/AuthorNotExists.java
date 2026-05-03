package org.example.bookreadingapp.exception.definitions;

public class AuthorNotExists extends RuntimeException {
    public AuthorNotExists(String message) {
        super(message);
    }
}
