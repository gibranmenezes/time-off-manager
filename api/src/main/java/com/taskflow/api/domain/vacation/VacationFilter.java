package com.taskflow.api.domain.vacation;

import com.taskflow.api.domain.enums.VacationStatus;

import java.time.LocalDate;

public record VacationFilter(Long vacationId, VacationStatus status,
                             LocalDate fromDate, LocalDate toDate, String collaboratorName) {
}
