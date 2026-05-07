package org.example.bookreadingapp.exception.definitions;

public class BookApiException extends RuntimeException {
    private final int statusCode;

    public BookApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public BookApiException(String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}

