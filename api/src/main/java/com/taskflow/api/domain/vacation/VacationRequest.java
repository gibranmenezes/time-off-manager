package com.taskflow.api.domain.vacation;

import java.time.LocalDate;

public record VacationRequest(Long collaboratorId, LocalDate startDate, LocalDate endDate) {
}
