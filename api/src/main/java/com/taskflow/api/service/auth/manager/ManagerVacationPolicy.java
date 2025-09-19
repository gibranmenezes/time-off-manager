package com.taskflow.api.service.auth.manager;

import com.taskflow.api.domain.enums.Role;
import com.taskflow.api.domain.user.User;
import com.taskflow.api.domain.vacation.Vacation;
import com.taskflow.api.service.AccessScopeService;
import com.taskflow.api.service.auth.AuthorizationPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ManagerVacationPolicy implements AuthorizationPolicy<Vacation> {
    
    private final AccessScopeService accessScopeService;
    
    @Override
    public boolean hasAccess(User user, Vacation resource) {
        if (user.getRole() != Role.MANAGER) {
            return false;
        }
        
        var scope = accessScopeService.resolve(user);
        return resource.getCollaborator().getManager() != null && 
               resource.getCollaborator().getManager().getId().equals(scope.managerId());
    }
}