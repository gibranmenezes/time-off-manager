package com.taskflow.api.domain.collaborator;

import java.time.Instant;

public record CollaboratorDetails(Long id, String name, String email, String department, String managerName, Instant createdAt) {
}
