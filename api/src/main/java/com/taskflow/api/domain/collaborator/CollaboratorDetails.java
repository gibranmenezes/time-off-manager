package com.taskflow.api.domain.collaborator;

import com.taskflow.api.domain.enums.Role;

import java.time.Instant;

public record CollaboratorDetails(Long id, String name, String email, String department, Role role , String managerName, Instant createdAt) {
}
