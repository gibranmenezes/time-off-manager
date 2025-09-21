package com.taskflow.api.service.validation.vacation;

import com.taskflow.api.domain.vacation.Vacation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CancellationDateValidation implements VacationCancellationValidation {
    @Override
    public void validate(Vacation vacation) {
        if (vacation.getStartDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("The vacation already starts and cannot be cancelled");
        }

    }
}
