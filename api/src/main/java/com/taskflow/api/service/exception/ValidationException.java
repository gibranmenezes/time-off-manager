package com.taskflow.api.service.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {

    private final ValidationErrorType errorType;

    public ValidationException(String message, ValidationErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public enum ValidationErrorType {
        INVALID_INPUT,
        RESOURCE_NOT_FOUND,
        PERMISSION_DENIED,
        BUSINESS_RULE_VIOLATION
    }
}
