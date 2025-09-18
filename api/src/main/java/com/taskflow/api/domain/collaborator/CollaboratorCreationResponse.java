package com.taskflow.api.domain.collaborator;

import com.taskflow.api.domain.user.User;

import java.time.Instant;

public record CollaboratorCreationResponse(Long id, String username, Instant createdAt){
}
