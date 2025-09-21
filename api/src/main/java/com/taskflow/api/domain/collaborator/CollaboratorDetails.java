package com.taskflow.api.domain.collaborator;

import com.taskflow.api.domain.enums.Role;

import java.time.Instant;

public record CollaboratorDetails(Long id, String name,String username, String email, String department, Role role , boolean active, String managerName, Instant createdAt) {
}
