package com.taskflow.api.domain.collaborator;

import com.taskflow.api.domain.user.User;

import java.time.Instant;

public record CollaboratorCreationResponse(Long collaboratorId, String username, Long managerId, Instant createdAt){
}
