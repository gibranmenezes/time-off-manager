package com.taskflow.api.service.policy;

import com.taskflow.api.domain.enums.Role;
import com.taskflow.api.domain.user.User;
import com.taskflow.api.domain.vacation.Vacation;
import org.springframework.stereotype.Component;

/**
 * Policy that grants access to admins for any vacation.
 */
@Component
public class AdminVacationPolicy implements AuthorizationPolicy<Vacation> {
    
    @Override
    public boolean hasAccess(User user, Vacation resource) {
        return user.getRole() == Role.ADMIN;
    }
}