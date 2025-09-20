package com.taskflow.api.service;

import com.taskflow.api.domain.enums.VacationStatus;
import com.taskflow.api.domain.user.User;
import com.taskflow.api.domain.vacation.Vacation;
import com.taskflow.api.domain.vacation.VacationRequest;
import com.taskflow.api.domain.vacation.VacationResponse;
import com.taskflow.api.mapper.VacationMapper;
import com.taskflow.api.respository.CollaboratorRepository;
import com.taskflow.api.respository.VacationRepository;
import com.taskflow.api.service.policy.VacationPolicyFactory;
import com.taskflow.api.service.validation.vacation.VacationCancellationValidation;
import com.taskflow.api.service.validation.vacation.VacationRequestValidation;
import com.taskflow.api.service.validation.vacation.VacationResponseValidation;

import com.taskflow.api.service.validation.vacation.PendingStatusValidation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VacationService {

    private final VacationRepository vacationRepository;
    private final CollaboratorRepository collaboratorRepository;
    private final AccessScopeService accessScopeService;

    private final List<VacationRequestValidation> requestValidations;
    private final List<VacationResponseValidation> responseValidations;
    private final List<VacationCancellationValidation> cancelValidations;

    private final PendingStatusValidation pendingStatusValidation;

    private final AuthorizationService authorizationService;
    private final VacationPolicyFactory policyFactory;

    private final VacationMapper mapper;
    public VacationResponse requestVacation(VacationRequest request, User currentUser) {
        var collaborator = collaboratorRepository.findById(request.collaboratorId())
                .orElseThrow(() -> new EntityNotFoundException("Collaborator not found"));

        authorizationService.checkAccess(
                currentUser,
                collaborator,
                policyFactory.getRequestPolicies(),
                "You don't have permission to request vacation for this collaborator"
        );

        requestValidations.forEach(v -> v.validate(collaborator, request.startDate(), request.endDate()));

        var vacation = Vacation.builder()
                .collaborator(collaborator)
                .startDate(request.startDate())
                .endDate(request.endDate())
                .build();

        var saved = vacationRepository.save(vacation);

        return mapper.toResponse(saved);
    }

    @Transactional
    public void approveOrRejectVacationRequest(Long requestId, String response, User currentUser) {
        var vacationRequest = vacationRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Vacation request not found"));

        authorizationService.checkAccess(
                currentUser,
                vacationRequest,
                policyFactory.getApprovalPolicies(),
                "You don't have permission to approve or reject this vacation request"
        );
        
        responseValidations.forEach(v -> v.validate(vacationRequest));
        
        vacationRequest.setResult(response);
        vacationRepository.save(vacationRequest);
    }

    @Transactional
    public void cancelVacation(Long vacationId, User currentUser) {
        var vacation = vacationRepository.findById(vacationId)
                .orElseThrow(() -> new EntityNotFoundException("Vacation not found"));

        cancelValidations.forEach(v -> v.validate(vacation));

        authorizationService.checkAccess(
                currentUser,
                vacation,
                policyFactory.getCancelPolicies(),
                "You don't have permission to cancel this vacation request"
        );

        vacationRepository.delete(vacation);
    }

    @Transactional
    public void updateVacationPeriod(Long vacationId, LocalDate startDate, LocalDate endDate, User currentUser) {
        var vacation = vacationRepository.findById(vacationId)
                .orElseThrow(() -> new EntityNotFoundException("Vacation not found"));

        authorizationService.checkAccess(
                currentUser,
                vacation.getCollaborator(),
                policyFactory.getRequestPolicies(),
                "You don't have permission to update this vacation request"
        );

        pendingStatusValidation.validate(vacation);
        requestValidations.forEach(v -> v.validate(vacation.getCollaborator(), startDate, endDate));

        vacation.changeVacationPeriod(startDate, endDate);
       vacationRepository.save(vacation);
    }
    
    public VacationResponse getVacationById(Long vacationId, User currentUser) {
        var vacation = vacationRepository.findById(vacationId)
                .orElseThrow(() -> new EntityNotFoundException("Vacation not found"));
        
        authorizationService.checkAccess(
                currentUser, 
                vacation, 
                policyFactory.getViewPolicies(),
                "You don't have permission to view this vacation request"
        );
        
        return mapper.toResponse(vacation);
    }

    public Page<VacationResponse> listVacations(User currentUser, VacationStatus status,
                                                LocalDate fromDate, LocalDate toDate,
                                                int page,int size) {

        var accessScope = accessScopeService.resolve(currentUser);
        
        var pageable = PageRequest.of(page, size);
        
        Page<Vacation> vacations = vacationRepository.findAllByScopeAndFilters(
                accessScope.managerId(),
                accessScope.collaboratorId(),
                status,
                fromDate,
                toDate,
                pageable
        );
        
        return vacations.map(mapper::toResponse);
    }
     
}
