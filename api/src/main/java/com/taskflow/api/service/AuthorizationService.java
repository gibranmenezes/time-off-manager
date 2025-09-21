package com.taskflow.api.service;

import com.taskflow.api.config.JwtTokenProvider;
import com.taskflow.api.domain.exception.ValidationErrorType;
import com.taskflow.api.domain.exception.ValidationException;
import com.taskflow.api.domain.user.User;
import com.taskflow.api.service.policy.AuthorizationPolicy;
import com.taskflow.api.web.dtos.LoginRequest;
import com.taskflow.api.web.dtos.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public TokenResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        Authentication auth = authenticationManager.authenticate(usernamePassword);
        User user = (User) auth.getPrincipal();
        String token = tokenProvider.generateToken(user);
        return new TokenResponse(token, user.getRole());
    }

    public <T> void checkAccess(User user, T resource, List<AuthorizationPolicy<T>> policies, String errorMessage) {
        boolean hasAccess = policies.stream()
                .anyMatch(policy -> policy.hasAccess(user, resource));
        
        if (!hasAccess) {
            throw new ValidationException(
                    errorMessage,
                    ValidationErrorType.PERMISSION_DENIED
            );
        }
    }
}