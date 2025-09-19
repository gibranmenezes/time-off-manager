package com.taskflow.api.service;

import com.taskflow.api.domain.exception.ValidationException;
import com.taskflow.api.domain.user.User;
import com.taskflow.api.service.auth.AuthorizationPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AuthorizationService {

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