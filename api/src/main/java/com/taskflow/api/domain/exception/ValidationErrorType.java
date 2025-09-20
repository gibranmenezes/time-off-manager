package com.taskflow.api.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ValidationErrorType {
    INVALID_INPUT("Validation error", HttpStatus.BAD_REQUEST, "VALIDATION_ERROR"),
    RESOURCE_NOT_FOUND("Resource not found", HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND"),
    PERMISSION_DENIED("Permission denied", HttpStatus.FORBIDDEN, "PERMISSION_DENIED"),
    BUSINESS_RULE_VIOLATION("Business rule violation", HttpStatus.UNPROCESSABLE_ENTITY, "BUSINESS_RULE_VIOLATION");

    private final String message;
    private final HttpStatus status;
    private final String code;
}
