package com.taskflow.api.web.controller;

import com.taskflow.api.domain.collaborator.*;
import com.taskflow.api.service.CollaboratorService;
import com.taskflow.api.web.dtos.AppResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collaborators")
@RequiredArgsConstructor
@Tag(name = "Collaborators", description = "Endpoints for managing collaborators")
public class CollaboratorController {

    private final CollaboratorService collaboratorService;

    @Operation(summary = "Create a new device", description = "Adds a new device to the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Device created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid device data provided",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppResponse.class)))
    })
    @PostMapping
    public ResponseEntity<AppResponse<CollaboratorCreationResponse>> register(@RequestBody @Valid CollaboratorCreationRequest request) {
        var response = collaboratorService.createCollaborator(request);
        return AppResponse.created("User registered successfully", response).getResponseEntity();

    }

    @Operation(
        summary = "List collaborators",
        description = "Returns a paginated list of collaborators. Supports filtering by name, username, email, department, manager, creation date, and active status."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Collaborators found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class)))
    })
    @GetMapping
    public ResponseEntity<AppResponse<List<CollaboratorDetails>>> getCollaborators(@ModelAttribute CollaboratorFilter filter, @RequestParam(defaultValue = "0") int page,
                                                                                   @RequestParam(defaultValue = "10") int size) {
        var collaborators = collaboratorService.getAllCollaborators(filter,page, size);
        return AppResponse.ok("Employees found", collaborators.getContent())
                .buildParametersPagination(collaborators.getNumber(), collaborators.getSize(), collaborators.getTotalElements(), collaborators.getTotalPages())
                .getResponseEntity();
    }

    @Operation(
        summary = "Get collaborator by ID",
        description = "Returns details of a collaborator by their ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Collaborator found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))),
        @ApiResponse(responseCode = "404", description = "Collaborator not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<AppResponse<CollaboratorDetails>> getCollaboratorById(@PathVariable Long id) {
        var collaborator = collaboratorService.getCollaboratorById(id);
        return AppResponse.ok("Collaborator found!", collaborator).getResponseEntity();
    }

    @Operation(
        summary = "Update collaborator",
        description = "Updates the details of a collaborator by their ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Collaborator updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid update data",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))),
        @ApiResponse(responseCode = "404", description = "Collaborator not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class)))
    })
    @PatchMapping("/{id}")
    public ResponseEntity<AppResponse<Void>> updateEmployee(@PathVariable Long id, CollaboratorUpdateRequest request) {
        collaboratorService.updateCollaborator(id, request);
        return AppResponse.<Void>ok("Collaborator updated successfully!", null).getResponseEntity();
    }

    @Operation(
        summary = "Delete collaborator",
        description = "Deletes a collaborator by their ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Collaborator deleted successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))),
        @ApiResponse(responseCode = "404", description = "Collaborator not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<AppResponse<Void>>  deleteCollaborator(@PathVariable Long id) {
        collaboratorService.deleteCollaborator(id);
        return AppResponse.<Void>ok("Collaborator excluded successfully!", null).getResponseEntity();
    }
}
