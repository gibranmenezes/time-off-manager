package com.taskflow.api.domain.collaborator;

import com.taskflow.api.domain.user.User;

import java.time.Instant;

public record CollaboratorCreationResponse(String id, String username, Instant createdAt){
    public CollaboratorCreationResponse(Collaborator collaborator, User user){
        this(collaborator.getId().toString(), user.getUsername(), collaborator.getCreatedAt());
    }
}
