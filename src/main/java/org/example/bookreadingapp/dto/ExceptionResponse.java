package org.example.bookreadingapp.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ExceptionResponse {
    private String message;
    private HttpStatus status;

    public ExceptionResponse buildExceptionResponse(HttpStatus status, String message){
        this.status = status;
        this.message = message;
        return this;
    }
}
