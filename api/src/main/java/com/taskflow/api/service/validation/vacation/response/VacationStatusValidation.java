package com.taskflow.api.service.validation.vacation.response;

import com.taskflow.api.domain.enums.VacationStatus;
import com.taskflow.api.domain.exception.ValidationException;
import com.taskflow.api.domain.vacation.Vacation;
import org.springframework.stereotype.Component;

@Component
public class VacationStatusValidation implements VacationResponseValidation {

    @Override
    public void validate(Vacation vacation) {
        if (vacation.getVacationStatus() != VacationStatus.PENDING) {
            throw new ValidationException(
                "Only vacation requests with PENDING status can be approved or rejected",
                ValidationException.ValidationErrorType.BUSINESS_RULE_VIOLATION
            );
        }
    }
}