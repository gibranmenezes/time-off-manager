package com.taskflow.api.service.validation.vacation.cancel;

import com.taskflow.api.domain.enums.VacationStatus;
import com.taskflow.api.domain.exception.ValidationException;
import com.taskflow.api.domain.vacation.Vacation;
import org.springframework.stereotype.Component;


@Component
public class CancellationStatusValidation {
    
    public void validate(Vacation vacation) {
        if (vacation.getVacationStatus() != VacationStatus.PENDING && 
            vacation.getVacationStatus() != VacationStatus.APPROVED) {
            throw new ValidationException(
                "Only pending or approved vacation requests can be cancelled",
                ValidationException.ValidationErrorType.BUSINESS_RULE_VIOLATION
            );
        }
    }
}