package org.example.bookreadingapp.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.example.bookreadingapp.dto.ExceptionResponse;
import org.example.bookreadingapp.exception.definitions.EmailExists;
import org.example.bookreadingapp.exception.definitions.WrongCredentials;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class AuthExceptionHandler {
    @ExceptionHandler(EmailExists.class)
    public ResponseEntity<ExceptionResponse> handleEmailExists(EmailExists e){
        log.error(e.getMessage());
        ExceptionResponse response = new ExceptionResponse();
        response.buildExceptionResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(WrongCredentials.class)
    public ResponseEntity<ExceptionResponse> handleWrongCredentials(WrongCredentials e){
        log.error(e.getMessage());
        ExceptionResponse response = new ExceptionResponse();
        response.buildExceptionResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
