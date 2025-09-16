package com.taskflow.api.web.controller;

import com.taskflow.api.domain.employee.EmployeeCreationRequest;
import com.taskflow.api.domain.employee.EmployeeCreationResponse;
import com.taskflow.api.domain.employee.EmployeeDetails;
import com.taskflow.api.service.EmployeeService;
import com.taskflow.api.web.reponse.AppResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<AppResponse<EmployeeCreationResponse>> registerEmployee(@RequestBody EmployeeCreationRequest request) {
        var response = employeeService.registerEmployee(request);
        return AppResponse.created("Employee created successfully", response).getResponseEntity();

    }

    @GetMapping
    public ResponseEntity<AppResponse<List<EmployeeDetails>>> getEmployees( @RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(defaultValue = "10") int size) {
        var employees = employeeService.getEmployees(page, size);

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
