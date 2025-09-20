package com.taskflow.api.service.validation.vacation;

import com.taskflow.api.domain.vacation.Vacation;

public interface VacationResponseValidation {
    void validate(Vacation vacation);
}