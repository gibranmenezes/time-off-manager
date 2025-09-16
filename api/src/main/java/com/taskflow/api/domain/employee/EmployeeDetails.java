package com.taskflow.api.domain.employee;

import java.time.Instant;
import java.util.UUID;

public record EmployeeDetails(UUID id, String name, String email, String department, String managerName, Instant createdAt) {

    public EmployeeDetails(Employee employee) {
        this(employee.getId(), employee.getName(), employee.getUser().getEmail(), employee.getDepartment(),
             employee.getManager().getName(), employee.getCreatedAt());
    }
}
