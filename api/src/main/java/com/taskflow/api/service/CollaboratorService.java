package com.taskflow.api.service;

import com.taskflow.api.domain.collaborator.Collaborator;
import com.taskflow.api.domain.collaborator.CollaboratorCreationRequest;
import com.taskflow.api.domain.collaborator.CollaboratorCreationResponse;
import com.taskflow.api.domain.collaborator.CollaboratorDetails;
import com.taskflow.api.domain.user.User;
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


    public CollaboratorCreationResponse registerEmployee(CollaboratorCreationRequest request) {
        var userToPersist = User.builder()
                .username(request.username())
                .email(request.email())
                .password(request.password())
                .build();

        var userPersisted = userRepository.save(userToPersist);

        var manager = this.findById(request.managerId());

        var employee = Collaborator.builder()
                .name(request.name())
                .user(userPersisted)
                .manager(manager)
                .department(request.department())
                .build();

        return new CollaboratorCreationResponse(employee,userPersisted);

    }

    public Collaborator findById(UUID id) {
        return collaboratorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Employee not found"));
    }

    public Page<CollaboratorDetails> getEmployees(int page, int size) {
        var pageable = PageRequest.of(page, size);

        Page<Collaborator> employees = collaboratorRepository.findAll(pageable);

        return employees.map(CollaboratorDetails::new);

    }
}
