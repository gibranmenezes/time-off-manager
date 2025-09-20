package com.taskflow.api.service.policy;

import com.taskflow.api.domain.collaborator.Collaborator;
import com.taskflow.api.domain.vacation.Vacation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class VacationPolicyFactory {

    private final AdminVacationPolicy adminPolicy;
    private final ManagerVacationPolicy managerPolicy;
    private final CollaboratorVacationPolicy collaboratorPolicy;
    private final ManagerApprovalPolicy managerApprovalPolicy;
    private final CollaboratorRequestPolicy collaboratorRequestPolicy;
    
 
    public List<AuthorizationPolicy<Vacation>> getViewPolicies() {
        return List.of(adminPolicy, managerPolicy, collaboratorPolicy);
    }
    
 
    public List<AuthorizationPolicy<Vacation>> getApprovalPolicies() {
        return List.of(adminPolicy, managerApprovalPolicy);
    }
    

    public List<AuthorizationPolicy<Vacation>> getCancelPolicies() {
        return List.of(adminPolicy, managerPolicy, collaboratorPolicy);
    }
    
  
    public List<AuthorizationPolicy<Collaborator>> getRequestPolicies() {
        return List.of(collaboratorRequestPolicy);
    }
}