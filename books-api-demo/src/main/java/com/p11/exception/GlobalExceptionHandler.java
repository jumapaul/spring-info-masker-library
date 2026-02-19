package com.p11.exception;

import com.p11.models.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(ResourceNotFoundException exception) {
        return new ResponseEntity<>(new ApiResponse<>(
                false, exception.getMessage(), null, HttpStatus.NOT_FOUND.value()
        ), HttpStatus.NOT_FOUND);
    }

}