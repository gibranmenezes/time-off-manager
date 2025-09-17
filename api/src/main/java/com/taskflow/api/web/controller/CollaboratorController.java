package com.taskflow.api.web.controller;

import com.taskflow.api.domain.collaborator.CollaboratorCreationRequest;
import com.taskflow.api.domain.collaborator.CollaboratorCreationResponse;
import com.taskflow.api.domain.collaborator.CollaboratorDetails;
import com.taskflow.api.domain.collaborator.CollaboratorUpdateRequest;
import com.taskflow.api.service.CollaboratorService;
import com.taskflow.api.web.reponse.AppResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class CollaboratorController {

    private final CollaboratorService collaboratorService;

    @PostMapping
    public ResponseEntity<AppResponse<CollaboratorCreationResponse>> register(@RequestBody CollaboratorCreationRequest request) {
        var response = collaboratorService.createCollaborator(request);
        return AppResponse.created("User registered successfully", response).getResponseEntity();

    }

    @GetMapping
    public ResponseEntity<AppResponse<List<CollaboratorDetails>>> getCollaborators(@RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "10") int size) {
        var collaborators = collaboratorService.getAllCollaborators(page, size);

        return AppResponse.ok("Employees found", collaborators.getContent())
                .buildParametersPagination(collaborators.getNumber(), collaborators.getSize(), collaborators.getTotalElements(), collaborators.getTotalPages())
                .getResponseEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppResponse<CollaboratorDetails>> getCollaboratorById(@PathVariable UUID id) {
        var collaborator = collaboratorService.getCollaboratorById(id);

        return AppResponse.ok("Collaborator found!", collaborator).getResponseEntity();

    }

    @PatchMapping("/{id}")
    public ResponseEntity<AppResponse<Void>> updateEmployee(@PathVariable UUID id, @RequestBody CollaboratorUpdateRequest request) {
        collaboratorService.updateCollaborator(id, request);

        return AppResponse.<Void>ok("Collaborator updated successfully!", null).getResponseEntity();

    }

    public void deleteCollaborator() {}
}
