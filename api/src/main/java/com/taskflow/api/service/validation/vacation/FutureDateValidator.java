package com.taskflow.api.service.validation.vacation;

import com.taskflow.api.domain.collaborator.Collaborator;
import com.taskflow.api.domain.exception.ValidationErrorType;
import com.taskflow.api.domain.exception.ValidationException;
import com.taskflow.api.domain.vacation.Vacation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class FutureDateValidator implements VacationRequestValidation, VacationUpdateValidation {

    @Override
    public void validate(Collaborator collaborator, LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();
        
        if (startDate.isBefore(today) || endDate.isBefore(today)) {
            throw new ValidationException(
                "Vacation dates must be in the future",
                ValidationErrorType.BUSINESS_RULE_VIOLATION
            );
        }
    }

    @Override
    public void validateUpdate(Vacation vacation) {
        validate(vacation.getCollaborator(), vacation.getStartDate(), vacation.getEndDate());
    }
}