package com.taskflow.api.domain.vacation;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record VacationRequest(@NotNull Long collaboratorId, @NotNull LocalDate startDate, @NotNull LocalDate endDate) {
}
