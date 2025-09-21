package com.taskflow.api.service.policy;

import com.taskflow.api.domain.collaborator.Collaborator;
import com.taskflow.api.domain.enums.Role;
import com.taskflow.api.domain.user.User;
import com.taskflow.api.service.AccessScopeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VacationCreationPolicy implements AuthorizationPolicy<Collaborator> {

    private final AccessScopeService accessScopeService;


    @Override
    public boolean hasAccess(User user, Collaborator collaborator) {
        if (user.getRole() == Role.ADMIN) {
            return true;
        }

        var scope = accessScopeService.resolve(user);

        return switch (user.getRole()) {
            case MANAGER -> collaborator.getManager() != null &&
                    collaborator.getManager().getId().equals(scope.managerId());
            case COLLABORATOR -> collaborator.getId().equals(scope.collaboratorId());
            default -> false;
        };
    }
}