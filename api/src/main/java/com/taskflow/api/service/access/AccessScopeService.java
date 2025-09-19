package com.taskflow.api.service.access;

import com.taskflow.api.domain.enums.Role;
import com.taskflow.api.domain.user.User;
import com.taskflow.api.respository.CollaboratorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class AccessScopeService {

    private final CollaboratorRepository collaboratorRepository;

    public AccessScope resolve(User currentUser) {
        if (currentUser == null || currentUser.getRole() == null) {
            throw new AccessDeniedException("User without defined role.");
        }

        Role role = currentUser.getRole();
        return switch (role) {
            case ADMIN -> AccessScope.admin();
            case MANAGER -> AccessScope.manager(fetchCollaboratorIdByUser(currentUser.getId(), true));
            case COLLABORATOR -> AccessScope.collaborator(fetchCollaboratorIdByUser(currentUser.getId(), false));
        };
    }

    private Long fetchCollaboratorIdByUser(Long userId, boolean isManager) {
        return collaboratorRepository.findCollaboratorIdByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        isManager
                                ? "Collaborator (manager) not found for current user."
                                : "Collaborator not found for current user."
                ));
    }


    public record AccessScope(Long managerId, Long collaboratorId) {
        public static AccessScope admin() {
            return new AccessScope(null, null);
        }
        public static AccessScope manager(Long managerId) {
            return new AccessScope(managerId, null);
        }
        public static AccessScope collaborator(Long collaboratorId) {
            return new AccessScope(null, collaboratorId);
        }
    }
}