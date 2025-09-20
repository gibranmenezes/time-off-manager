package com.taskflow.api.web.controller;

import com.taskflow.api.domain.enums.VacationStatus;
import com.taskflow.api.domain.user.User;
import com.taskflow.api.domain.vacation.VacationRequest;
import com.taskflow.api.domain.vacation.VacationResponse;
import com.taskflow.api.service.VacationService;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/vacations")
@RequiredArgsConstructor
@Tag(name = "Vacations", description = "Endpoints for managing vacations")
public class VacationController {

    private final VacationService vacationService;

    @Operation(
        summary = "Create vacation request",
        description = "Creates a new vacation request for the authenticated user."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Vacation request created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid vacation request data",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class)))
    })
    @PostMapping
    public ResponseEntity<AppResponse<VacationResponse>> createVacationRequest(@RequestBody @Valid VacationRequest request) {
        var currentUser = getCurrentUser();
        var response = vacationService.requestVacation(request, currentUser);

        return AppResponse.created("Vacation request created successfully", response).getResponseEntity();
    }

    @Operation(
        summary = "List vacations",
        description = "Returns a paginated list of vacation requests for the authenticated user. Supports filtering by status and date range."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vacations found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class)))
    })
    @GetMapping
    public ResponseEntity<AppResponse<List<VacationResponse>>> listVacations(
            @RequestParam(required = false) VacationStatus status,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        User currentUser = getCurrentUser();
        var vacations = vacationService.listVacations(currentUser, status, fromDate, toDate, page, size);
        return AppResponse.ok("Vacations found", vacations.getContent())
                .buildParametersPagination(vacations.getNumber(), vacations.getSize(), vacations.getTotalElements(), vacations.getTotalPages())
                .getResponseEntity();
    }

    @Operation(
        summary = "Get vacation by ID",
        description = "Returns details of a vacation request by its ID for the authenticated user."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vacation found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))),
        @ApiResponse(responseCode = "404", description = "Vacation not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<AppResponse<VacationResponse>> getVacationById(@PathVariable Long id) {
        User currentUser = getCurrentUser();
        var vacation = vacationService.getVacationById(id, currentUser);
        return AppResponse.ok("Vacation found", vacation).getResponseEntity();
    }

    @Operation(
        summary = "Update vacation period",
        description = "Updates the start and/or end date of a vacation request by its ID for the authenticated user."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vacation updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid update data",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))),
        @ApiResponse(responseCode = "404", description = "Vacation not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class)))
    })
    @PatchMapping("/{id}")
    public ResponseEntity<AppResponse<Void>> updateVacation (
            @PathVariable Long id,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        
        User currentUser = getCurrentUser();
        
        vacationService.updateVacationPeriod(id, startDate, endDate, currentUser);
        return AppResponse.<Void>ok("Vacation updated successfully", null).getResponseEntity();
    }

    @Operation(
        summary = "Approve or reject vacation request",
        description = "Approves or rejects a vacation request by its ID for the authenticated user (manager or admin)."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Response saved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid response data",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))),
        @ApiResponse(responseCode = "404", description = "Vacation not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class)))
    })
    @PutMapping("/{id}/response")
    public ResponseEntity<AppResponse<Void>> respondToVacationRequest(@PathVariable Long id, @RequestBody String response) {
        vacationService.approveOrRejectVacationRequest(id, response, getCurrentUser());
        return AppResponse.<Void>ok("Response saved successfully", null).getResponseEntity();
    }

    @Operation(
        summary = "Cancel vacation request",
        description = "Cancels a vacation request by its ID for the authenticated user."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vacation canceled successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))),
        @ApiResponse(responseCode = "404", description = "Vacation not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<AppResponse<Void>> cancelVacation(@PathVariable Long id) {
        User currentUser = getCurrentUser();
        
        vacationService.cancelVacation(id, currentUser);
        return AppResponse.<Void>ok("Vacation canceled successfully", null).getResponseEntity();
    }


    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new RuntimeException("Usuário não autenticado");
        }
        return (User) authentication.getPrincipal();
    }
}