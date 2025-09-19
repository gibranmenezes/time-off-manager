package com.taskflow.api.service.auth.colladorator;

import com.taskflow.api.domain.collaborator.Collaborator;
import com.taskflow.api.domain.enums.Role;
import com.taskflow.api.domain.user.User;
import com.taskflow.api.service.AccessScopeService;
import com.taskflow.api.service.auth.AuthorizationPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Policy that grants access to collaborators for creating their own vacation requests.
 */
@Component
@RequiredArgsConstructor
public class CollaboratorRequestPolicy implements AuthorizationPolicy<Collaborator> {
    
    private final AccessScopeService accessScopeService;
    
    @Override
    public boolean hasAccess(User user, Collaborator collaborator) {
        if (user.getRole() == Role.ADMIN) {
            return true;
        }
        
        if (user.getRole() != Role.COLLABORATOR) {
            return false;
        }
        
        var scope = accessScopeService.resolve(user);
        return collaborator.getId().equals(scope.collaboratorId());
    }
}