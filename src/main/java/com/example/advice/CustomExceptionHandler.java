package com.example.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.exception.JwtException;
import com.example.payload.Response;

@RestControllerAdvice
public class CustomExceptionHandler {
    
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Response<Void>> handleJwtException(JwtException ex) {
        return ResponseEntity.status(ex.getStatus())
            .body(Response.fail(ex.getStatus(), ex.getMessage()));
    }
}
