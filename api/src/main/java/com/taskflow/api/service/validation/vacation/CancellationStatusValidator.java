package com.taskflow.api.service.validation.vacation;

import com.taskflow.api.domain.enums.VacationStatus;
import com.taskflow.api.domain.exception.ValidationErrorType;
import com.taskflow.api.domain.exception.ValidationException;
import com.taskflow.api.domain.vacation.Vacation;
import org.springframework.stereotype.Component;


@Component
public class CancellationStatusValidator implements  VacationCancellationValidation {
    
    @Override
    public void validate(Vacation vacation) {
        if (vacation.getVacationStatus() != VacationStatus.PENDING && 
            vacation.getVacationStatus() != VacationStatus.APPROVED) {
            throw new ValidationException(
                "Only pending or approved vacation requests can be cancelled",
                ValidationErrorType.BUSINESS_RULE_VIOLATION
            );
        }
    }
}