package com.taskflow.api.domain.collaborator;

import com.taskflow.api.domain.enums.Role;

import java.time.LocalDate;
import java.util.Objects;

public record CollaboratorFilter(Long collaboratorId, String name, String username, String email, String department, Long managerId,
                                 String managerName, Role userRole,
                                 LocalDate createdAtStart, LocalDate createdAtEnd, Boolean active) {
    public CollaboratorFilter {
        active = Objects.requireNonNullElse(active, true);
    }
}
