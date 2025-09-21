package com.taskflow.api.service.validation.vacation;

import com.taskflow.api.domain.enums.VacationStatus;
import com.taskflow.api.domain.exception.ValidationErrorType;
import com.taskflow.api.domain.exception.ValidationException;
import com.taskflow.api.domain.vacation.Vacation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class StatusAndDateValidator implements VacationCancellationValidation, VacationUpdateValidation {
    @Override
    public void validate(Vacation vacation) {
        if (vacation.getVacationStatus() == VacationStatus.APPROVED && vacation.getStartDate().isBefore(LocalDate.now())  ) {
            throw new ValidationException(
                    "This vacation was approved an has already started",
                    ValidationErrorType.BUSINESS_RULE_VIOLATION
            );
        }

    }

    @Override
    public void validateUpdate(Vacation vacation) {
        validate(vacation);
    }
}
