package com.taskflow.api.domain.employee;

import com.taskflow.api.domain.enums.Role;
import com.taskflow.api.domain.user.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.Instant;

public record EmployeeCreationResponse(String id, String username, Instant createdAt){
    public EmployeeCreationResponse(Employee employee, User user){
        this(employee.getId().toString(), user.getUsername(), employee.getCreatedAt());
    }
}
