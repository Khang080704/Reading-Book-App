package org.example.bookreadingapp.exception.definitions;

public class EmailExists extends RuntimeException {
    public EmailExists(String message) {
        super(message);
    }
}
