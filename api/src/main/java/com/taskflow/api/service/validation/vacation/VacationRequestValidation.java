package com.taskflow.api.service.validation.vacation;

import com.taskflow.api.domain.collaborator.Collaborator;

import java.time.LocalDate;

public interface VacationRequestValidation {
    void validate(Collaborator collaborator, LocalDate startDate, LocalDate endDate);
}