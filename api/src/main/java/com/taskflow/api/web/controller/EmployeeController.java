package com.taskflow.api.web.controller;

import com.taskflow.api.domain.collaborator.CollaboratorCreationRequest;
import com.taskflow.api.domain.collaborator.CollaboratorCreationResponse;
import com.taskflow.api.domain.collaborator.CollaboratorDetails;
import com.taskflow.api.service.CollaboratorService;
import com.taskflow.api.web.reponse.AppResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final CollaboratorService collaboratorService;

    @PostMapping
    public ResponseEntity<AppResponse<CollaboratorCreationResponse>> registerEmployee(@RequestBody CollaboratorCreationRequest request) {
        var response = collaboratorService.registerEmployee(request);
        return AppResponse.created("Employee created successfully", response).getResponseEntity();

    }

    @GetMapping
    public ResponseEntity<AppResponse<List<CollaboratorDetails>>> getEmployees(@RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "10") int size) {
        var employees = collaboratorService.getEmployees(page, size);

        return AppResponse.ok("Employees found", employees.getContent())
                .buildParametersPagination(employees.getNumber(), employees.getSize(), employees.getTotalElements(), employees.getTotalPages())
                .getResponseEntity();
    }

    public void getEmployeeById() {

    }

    public void updateEmployee() {

    }

    public void deleteEmployee() {}
}
