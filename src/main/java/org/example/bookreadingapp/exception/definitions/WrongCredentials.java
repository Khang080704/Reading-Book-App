package org.example.bookreadingapp.exception.definitions;

public class WrongCredentials extends RuntimeException {
    public WrongCredentials(String message) {
        super(message);
    }
}
