package com.taskflow.api.respository;

import com.taskflow.api.domain.collaborator.Collaborator;
import com.taskflow.api.domain.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface CollaboratorRepository extends JpaRepository<Collaborator, Long> {

    @Query("select c.id from Collaborator c where c.user.id = :userId")
    Optional<Long> findCollaboratorIdByUserId(Long userId);

    @Query("""
    select c
    from Collaborator c
    join c.user u
    where (:collaboratorId is null or c.id = :collaboratorId)
      and(:name is null or lower(c.name) like lower(concat('%', :name, '%')))
      and (:username is null or lower(u.username) like lower(concat('%', :username, '%')))
      and (:email is null or lower(u.email) like lower(concat('%', :email, '%')))
      and (:department is null or lower(c.department) like lower(concat('%', :department, '%')))
      and (:managerId is null or c.manager.id = :managerId)
      and (:createdFrom is null or c.createdAt >= :createdFrom)
      and (:createdTo is null or c.createdAt <= :createdTo)
      and (:userRole is null or u.role = :userRole)
      and (:active is null or c.active = :active)
    """)
    Page<Collaborator> findAllWithFilters(
            @Param("collaboratorId") Long collaboratorId,
            @Param("name") String name,
            @Param("username") String username,
            @Param("email") String email,
            @Param("department") String department,
            @Param("managerId") Long managerId,
            @Param("createdFrom") Instant createdFrom,
            @Param("createdTo") Instant createdTo,
            @Param("userRole") Role userRole,
            @Param("active") Boolean active,
            Pageable pageable
    );
}
