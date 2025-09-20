package com.taskflow.api.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ValidationException extends RuntimeException {
    
    private final ValidationErrorType errorType;
    
    public ValidationException(String message, ValidationErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }


}