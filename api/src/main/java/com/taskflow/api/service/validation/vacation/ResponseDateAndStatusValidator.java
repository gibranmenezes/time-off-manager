package com.taskflow.api.service.validation.vacation;

import com.taskflow.api.domain.enums.VacationStatus;
import com.taskflow.api.domain.exception.ValidationErrorType;
import com.taskflow.api.domain.exception.ValidationException;
import com.taskflow.api.domain.vacation.Vacation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ResponseDateAndStatusValidator implements VacationResponseValidation {

    @Override
    public void validate(Vacation vacation) {
        if (vacation.getVacationStatus() != VacationStatus.PENDING && vacation.getStartDate().isBefore(LocalDate.now())) {
            throw new ValidationException(
                "Only vacation requests with PENDING status can be approved or rejected",
                ValidationErrorType.BUSINESS_RULE_VIOLATION
            );
        }
    }
}