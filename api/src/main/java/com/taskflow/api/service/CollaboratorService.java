package com.taskflow.api.service;

import com.taskflow.api.domain.collaborator.*;
import com.taskflow.api.domain.enums.Role;
import com.taskflow.api.mapper.CollaboratorMapper;
import com.taskflow.api.respository.CollaboratorRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CollaboratorService {


    private final CollaboratorRepository collaboratorRepository;
    private final UserService userService;
    private final CollaboratorMapper mapper;


    public CollaboratorCreationResponse createCollaborator(CollaboratorCreationRequest request) {
        var user = userService.createUser(request.username(), request.email(), request.password(), request.role());
        var manager = getAssociateManager(request);

        var collaborator = Collaborator.builder()
                .name(request.name())
                .user(user)
                .department(request.department())
                .manager(manager)
                .build();

        var saved = collaboratorRepository.save(collaborator);

        return mapper.toCreationResponse(saved);

    }

    public CollaboratorDetails getCollaboratorById(long id) {
        var collaborator =  collaboratorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Collaborator not found"));

        return mapper.toDetails(collaborator);
    }

    public Page<CollaboratorDetails> getAllCollaborators(int page, int size) {
        var pageable = PageRequest.of(page, size);

        Page<Collaborator> collaborators = collaboratorRepository.findAll(pageable);

        return collaborators.map(mapper::toDetails);

    }

    @Transactional
    public void updateCollaborator(Long id, CollaboratorUpdateRequest request) {
        var collaborator =  collaboratorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Collaborator not found"));

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
