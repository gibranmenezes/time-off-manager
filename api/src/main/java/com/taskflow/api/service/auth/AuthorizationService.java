package com.taskflow.api.service.auth;

import com.taskflow.api.domain.exception.ValidationException;
import com.taskflow.api.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service that centralizes authorization logic using policy pattern.
 * This service maintains a list of authorization policies and checks if any of them grant access.
 */
@Service
@RequiredArgsConstructor
public class AuthorizationService {

    /**
     * Checks if the user has access to the resource using a list of policies.
     * Access is granted if any policy returns true.
     *
     * @param user User attempting to access the resource
     * @param resource The resource being accessed
     * @param policies List of policies to check
     * @param errorMessage Error message to throw if access is denied
     * @throws ValidationException if access is denied
     */
    public <T> void checkAccess(User user, T resource, List<AuthorizationPolicy<T>> policies, String errorMessage) {
        boolean hasAccess = policies.stream()
                .anyMatch(policy -> policy.hasAccess(user, resource));
        
        if (!hasAccess) {
            throw new ValidationException(
                    errorMessage,
                    ValidationException.ValidationErrorType.PERMISSION_DENIED
            );
        }
    }
}