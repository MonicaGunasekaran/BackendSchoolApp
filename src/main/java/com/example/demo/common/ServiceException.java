package com.example.demo.common;
import org.springframework.http.HttpStatus;
public class ServiceException extends RuntimeException {
    private final HttpStatus status;
    public ServiceException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }
    public ServiceException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
    public HttpStatus getStatus() {
        return status;
    }
}
