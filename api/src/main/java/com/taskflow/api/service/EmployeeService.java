package com.taskflow.api.service;

import com.taskflow.api.domain.employee.Employee;
import com.taskflow.api.domain.employee.EmployeeCreationRequest;
import com.taskflow.api.domain.employee.EmployeeCreationResponse;
import com.taskflow.api.domain.employee.EmployeeDetails;
import com.taskflow.api.domain.user.User;
import com.taskflow.api.respository.EmployeeRepository;
import com.taskflow.api.respository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {


    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;


    public EmployeeCreationResponse registerEmployee(EmployeeCreationRequest request) {
        var userToPersist = User.builder()
                .username(request.username())
                .email(request.email())
                .password(request.password())
                .build();

        var userPersisted = userRepository.save(userToPersist);

        var manager = this.findById(request.managerId());

        var employee = Employee.builder()
                .name(request.name())
                .user(userPersisted)
                .manager(manager)
                .department(request.department())
                .build();

        return new EmployeeCreationResponse(employee,userPersisted);

    }

    public Employee findById(UUID id) {
        return employeeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Employee not found"));
    }

    public Page<EmployeeDetails> getEmployees(int page, int size) {
        var pageable = PageRequest.of(page, size);

        Page<Employee> employees = employeeRepository.findAll(pageable);

        return employees.map(EmployeeDetails::new);

    }
}
