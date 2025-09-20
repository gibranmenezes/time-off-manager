package com.taskflow.api.service.policy;

import com.taskflow.api.domain.enums.Role;
import com.taskflow.api.domain.user.User;
import com.taskflow.api.domain.vacation.Vacation;
import com.taskflow.api.service.AccessScopeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ManagerApprovalPolicy implements AuthorizationPolicy<Vacation> {
    
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