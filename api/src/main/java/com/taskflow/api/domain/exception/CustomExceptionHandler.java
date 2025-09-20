package com.taskflow.api.domain.exception;

import com.taskflow.api.web.dtos.AppErrorResponse;
import com.taskflow.api.web.dtos.AppResponse;
import io.micrometer.tracing.Tracer;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Objects;

@RestControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler {

    private final Tracer tracer;

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<AppResponse<Object>> handleValidationException(ValidationException ex) {
        var errorType = ex.getErrorType();
        var traceId = getTraceId();

        AppErrorResponse error = AppErrorResponse.builder()
                .code(errorType.getCode())
                .description(ex.getMessage())
                .traceId(traceId)
                .build();

        return AppResponse.invalid(errorType.getMessage(), errorType.getStatus(), Collections.singletonList(error)).getResponseEntity();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<AppResponse<Object>> handleEntityNotFoundException(EntityNotFoundException ex) {
        AppErrorResponse error = AppErrorResponse.builder()
                .code("ENTITY_NOT_FOUND")
                .description(ex.getMessage())
                .traceId(getTraceId())
                .build();

        return AppResponse.notFound("Entity not found", "ENTITY_NOT_FOUND", ex.getMessage(), getTraceId())
                .getResponseEntity();
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<AppResponse<Object>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        AppErrorResponse error = AppErrorResponse.builder()
                .code("AUTHENTICATION_FAILED")
                .description(ex.getMessage())
                .traceId(getTraceId())
                .build();

        return AppResponse.invalid("Authentication failed",
                        HttpStatus.UNAUTHORIZED,
                        Collections.singletonList(error))
                .getResponseEntity();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<AppResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        AppErrorResponse error = AppErrorResponse.builder()
                .code("INVALID_ARGUMENT")
                .description(ex.getMessage())
                .traceId(getTraceId())
                .build();

        return AppResponse.badRequest("Invalid argument", "INVALID_ARGUMENT", ex.getMessage(), getTraceId())
                .getResponseEntity();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<AppResponse<Object>> handleAccessDeniedException(AccessDeniedException ex) {
        AppErrorResponse error = AppErrorResponse.builder()
                .code("ACCESS_DENIED")
                .description(ex.getMessage())
                .traceId(getTraceId())
                .build();

        return AppResponse.invalid("Access denied",
                        HttpStatus.FORBIDDEN,
                        Collections.singletonList(error))
                .getResponseEntity();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<AppResponse<Object>> handleRuntimeException(RuntimeException ex) {

        if (ex.getMessage() != null && (ex.getMessage().contains("Token") || ex.getMessage().contains("token"))) {
            AppErrorResponse error = AppErrorResponse.builder()
                    .code("AUTHENTICATION_FAILED")
                    .description(ex.getMessage())
                    .traceId(getTraceId())
                    .build();

            return AppResponse.invalid("Authentication failed",
                            HttpStatus.UNAUTHORIZED,
                            Collections.singletonList(error))
                    .getResponseEntity();
        }

        AppErrorResponse error = AppErrorResponse.builder()
                .code("INTERNAL_ERROR")
                .description(ex.getMessage())
                .traceId(getTraceId())
                .build();

        return AppResponse.internalServerError("Internal server error", "INTERNAL_ERROR", ex.getMessage(), getTraceId())
                .getResponseEntity();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppResponse<Object>> handleGenericException(Exception ex) {
        AppErrorResponse error = AppErrorResponse.builder()
                .code("INTERNAL_ERROR")
                .description("An unexpected error occurred")
                .traceId(getTraceId())
                .build();

        return AppResponse.internalServerError("Internal server error", "INTERNAL_ERROR", "An unexpected error occurred",
                        getTraceId())
                .getResponseEntity();
    }

    private String getTraceId() {
        try {
            return Objects.requireNonNull(tracer.currentTraceContext().context().traceId());
        } catch (Exception e) {
            return  null;
        }
    }

}
