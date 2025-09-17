package com.taskflow.api.service;

import com.taskflow.api.domain.collaborator.*;
import com.taskflow.api.domain.user.User;
import com.taskflow.api.mapper.CollaboratorMapper;
import com.taskflow.api.respository.CollaboratorRepository;
import com.taskflow.api.respository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CollaboratorService {


    private final UserRepository userRepository;
    private final CollaboratorRepository collaboratorRepository;
    private final CollaboratorMapper mapper;


    public CollaboratorCreationResponse createCollaborator(CollaboratorCreationRequest request) {
        var userToPersist = User.builder()
                .username(request.username())
                .email(request.email())
                .password(request.password())
                .build();

        var userPersisted = userRepository.save(userToPersist);

        var manager = collaboratorRepository.findById(request.managerId())
                .orElseThrow(() -> new EntityNotFoundException("Manager not found"));

        var employee = Collaborator.builder()
                .name(request.name())
                .user(userPersisted)
                .manager(manager)
                .department(request.department())
                .build();

        return new CollaboratorCreationResponse(employee,userPersisted);

    }

    public CollaboratorDetails getCollaboratorById(UUID id) {
        var collaborator =  collaboratorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Collaborator not found"));

        return new CollaboratorDetails(collaborator);
    }

    public Page<CollaboratorDetails> getAllCollaborators(int page, int size) {
        var pageable = PageRequest.of(page, size);

        Page<Collaborator> employees = collaboratorRepository.findAll(pageable);

        return employees.map(CollaboratorDetails::new);

    }

    public void updateCollaborator(UUID id, CollaboratorUpdateRequest request) {
        var collaborator =  collaboratorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Collaborator not found"));

      mapper.updateCollaboratorFromDto(request, collaborator);

      collaboratorRepository.save(collaborator);

    }

    public void deleteCollaborator(UUID id) {
        var collaborator =  collaboratorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Collaborator not found"));

        collaborator.inactivate();
    }
}
