package org.example.bookreadingapp.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.example.bookreadingapp.dto.exception.ExceptionResponse;
import org.example.bookreadingapp.exception.definitions.AuthorNotExists;
import org.example.bookreadingapp.exception.definitions.EmailExists;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class AuthorExceptionHandler {
    @ExceptionHandler(AuthorNotExists.class)
    public ResponseEntity<ExceptionResponse> handleEmailExists(AuthorNotExists e){
        log.error(e.getMessage());
        ExceptionResponse response = new ExceptionResponse();
        response.buildExceptionResponse(HttpStatus.NOT_FOUND, e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
