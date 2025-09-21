package com.taskflow.api.service.validation.vacation;

import com.taskflow.api.domain.collaborator.Collaborator;
import com.taskflow.api.domain.exception.ValidationErrorType;
import com.taskflow.api.domain.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class VacationDurationValidator implements VacationRequestValidation {

    private static final int MAX_VACATION_DAYS = 30;

    @Override
    public void validate(Collaborator collaborator, LocalDate startDate, LocalDate endDate) {
        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        
        if (days > MAX_VACATION_DAYS) {
            throw new ValidationException(
                String.format("Vacation duration exxceeds the maximum limit of %d days", MAX_VACATION_DAYS),
                ValidationErrorType.BUSINESS_RULE_VIOLATION
            );
        }
    }
}