package com.taskflow.api.respository;

import com.taskflow.api.domain.collaborator.Collaborator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CollaboratorRepository extends JpaRepository<Collaborator, Long> {

    @Query("select c.id from Collaborator c where c.user.id = :userId")
    Optional<Long> findCollaboratorIdByUserId(Long userId);
}
