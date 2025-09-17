package com.taskflow.api.respository;

import com.taskflow.api.domain.collaborator.Collaborator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CollaboratorRepository extends JpaRepository<Collaborator, UUID> {


}
