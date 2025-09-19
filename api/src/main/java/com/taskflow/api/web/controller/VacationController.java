package com.taskflow.api.web.controller;

import com.taskflow.api.domain.enums.VacationStatus;
import com.taskflow.api.domain.user.User;
import com.taskflow.api.domain.vacation.VacationRequest;
import com.taskflow.api.domain.vacation.VacationResponse;
import com.taskflow.api.service.VacationService;
import com.taskflow.api.web.dtos.AppResponse;
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
public class VacationController {

    private final VacationService vacationService;

    @PostMapping
    public ResponseEntity<AppResponse<VacationResponse>> createVacationRequest(@RequestBody @Valid VacationRequest request) {
        var currentUser = getCurrentUser();
        var response = vacationService.requestVacation(request, currentUser);

        return AppResponse.created("Vacation request created successfully", response).getResponseEntity();
    }

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

    @GetMapping("/{id}")
    public ResponseEntity<AppResponse<VacationResponse>> getVacationById(@PathVariable Long id) {
        User currentUser = getCurrentUser();
        var vacation = vacationService.getVacationById(id, currentUser);
        return AppResponse.ok("Vacation found", vacation).getResponseEntity();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AppResponse<Void>> updateVacation (
            @PathVariable Long id,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        
        User currentUser = getCurrentUser();
        
        vacationService.updateVacationPeriod(id, startDate, endDate, currentUser);
        return AppResponse.<Void>ok("Vacation updated successfully", null).getResponseEntity();
    }

    @PostMapping("/{id}/response")
    public ResponseEntity<AppResponse<Void>> respondToVacationRequest(@PathVariable Long id, @RequestBody String response) {
        vacationService.approveOrRejectVacationRequest(id, response, getCurrentUser());
        return AppResponse.<Void>ok("Response saved successfully", null).getResponseEntity();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppResponse<Void>> cancelVacation(@PathVariable Long id) {
        User currentUser = getCurrentUser();
        
        vacationService.cancelVacation(id, currentUser);
        return AppResponse.<Void>ok("Vacation canceled successfully", null).getResponseEntity();
    }


    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}