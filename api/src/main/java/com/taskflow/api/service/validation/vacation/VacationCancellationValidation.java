package com.taskflow.api.service.validation.vacation;

import com.taskflow.api.domain.vacation.Vacation;

public interface VacationCancellationValidation {

    void validate(Vacation vacation);
}
