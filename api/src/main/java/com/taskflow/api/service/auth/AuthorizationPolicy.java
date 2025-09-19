package com.taskflow.api.service.auth;

import com.taskflow.api.domain.user.User;

/**
 * Interface for authorization policies.
 * Each policy implements a specific access rule.
 */
public interface AuthorizationPolicy<T> {
    /**
     * Checks if the user has access to the resource.
     *
     * @param user User attempting to access the resource
     * @param resource The resource being accessed
     * @return true if access is granted, false otherwise
     */
    boolean hasAccess(User user, T resource);
}