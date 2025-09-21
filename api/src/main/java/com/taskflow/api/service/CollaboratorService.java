package com.taskflow.api.service;

import com.taskflow.api.domain.collaborator.*;
import com.taskflow.api.domain.enums.Role;
import com.taskflow.api.domain.user.User;
import com.taskflow.api.mapper.CollaboratorMapper;
import com.taskflow.api.respository.CollaboratorRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class CollaboratorService {


    private final CollaboratorRepository collaboratorRepository;
    private final UserService userService;
    private final AccessScopeService accessScopeService;
    private final CollaboratorMapper mapper;


    public CollaboratorDetails createCollaborator(CollaboratorCreationRequest request) {
        var user = userService.createUser(request.username(), request.email(), request.password(), request.role());
        var manager = getAssociateManager(request);

        var collaborator = Collaborator.builder()
                .name(request.name())
                .user(user)
                .department(request.department())
                .manager(manager)
                .build();

        var saved = collaboratorRepository.save(collaborator);

        return mapper.toDetails(saved);

    }

    public Page<CollaboratorDetails> getCollaborators(CollaboratorFilter filter, User currentUser, int page, int size) {
        var pageable = PageRequest.of(page, size);


        var accessScope = accessScopeService.resolve(currentUser);

        var collaboratorId = currentUser.getRole() == Role.COLLABORATOR ? accessScope.collaboratorId() : filter.collaboratorId();

        Page<Collaborator> collaborators = collaboratorRepository.findAllWithFilters(
                collaboratorId,
                filter.name(),
                filter.username(),
                filter.email(),
                filter.department(),
                accessScope.managerId(),
                filter.createdAtStart() != null ? filter.createdAtStart().atStartOfDay(ZoneOffset.UTC).toInstant() : null,
                filter.createdAtEnd() != null ? filter.createdAtEnd().atTime(23, 59, 59).atZone(ZoneOffset.UTC).toInstant() : null,
                filter.userRole(),
                filter.active(),
                pageable
        );

        return collaborators.map(mapper::toDetails);

    }

    @Transactional
    public void updateCollaborator(Long id, CollaboratorUpdateRequest request) {
        var collaborator =  collaboratorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Collaborator not found"));

        var user = userService.updateUser(collaborator.getUser().getId(), request.username(), request.userEmail(), request.userPassword(), request.userRole());
        collaborator.setUser(user);
        mapper.updatedCollaboratorFromDto(request, collaborator);

        collaboratorRepository.save(collaborator);

    }

    @Transactional
    public void deleteCollaborator(Long id) {
        var collaborator =  collaboratorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Collaborator not found"));

        collaborator.inactivate();
        collaborator.getUser().inactivate();
    }

    private Collaborator getAssociateManager(CollaboratorCreationRequest request) {
        if (!request.role().equals(Role.MANAGER) && !request.role().equals(Role.ADMIN)) {
            return collaboratorRepository.findById(request.managerId())
                    .orElseThrow(() -> new EntityNotFoundException("Manager not found"));
        }
        return null;
    }

}
