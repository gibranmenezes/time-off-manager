package com.taskflow.api.service.auth.colladorator;

import com.taskflow.api.domain.enums.Role;
import com.taskflow.api.domain.user.User;
import com.taskflow.api.domain.vacation.Vacation;
import com.taskflow.api.service.access.AccessScopeService;
import com.taskflow.api.service.auth.AuthorizationPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Policy that grants access to collaborators for their own vacations.
 */
@Component
@RequiredArgsConstructor
public class CollaboratorVacationPolicy implements AuthorizationPolicy<Vacation> {
    
    private final AccessScopeService accessScopeService;
    
    @Override
    public boolean hasAccess(User user, Vacation resource) {
        if (user.getRole() != Role.COLLABORATOR) {
            return false;
        }
        
        var scope = accessScopeService.resolve(user);
        return resource.getCollaborator().getId().equals(scope.collaboratorId());
    }
}