package com.taskflow.api.service.validation.vacation.cancel;

import com.taskflow.api.domain.vacation.Vacation;

public interface VacationCancellationValidation {

    void validate(Vacation vacation);
}
