package com.example.exception;

import org.springframework.http.HttpStatus;

public class JwtException extends RuntimeException {
    
    private int status;

    public JwtException(String message) {
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public JwtException(HttpStatus status, String message) {
        super(message);
        this.status = status.value();
    }

    public int getStatus() {
        return status;
    }
}
