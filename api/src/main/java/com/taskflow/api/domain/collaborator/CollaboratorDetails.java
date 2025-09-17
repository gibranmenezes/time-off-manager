package com.taskflow.api.domain.collaborator;

import java.time.Instant;
import java.util.UUID;

public record CollaboratorDetails(UUID id, String name, String email, String department, String managerName, Instant createdAt) {

    public CollaboratorDetails(Collaborator collaborator) {
        this(collaborator.getId(), collaborator.getName(), collaborator.getUser().getEmail(), collaborator.getDepartment(),
             collaborator.getManager().getName(), collaborator.getCreatedAt());
    }
}
