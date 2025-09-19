package com.taskflow.api.domain.collaborator;

import com.taskflow.api.domain.enums.Role;

public record CollaboratorUpdateRequest(String username, String userEmail, String userPassword, Role userRole,
                                        String name, String department, Long managerId) {
}
