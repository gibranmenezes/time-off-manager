package com.taskflow.api.domain.vacation;

import com.taskflow.api.domain.enums.VacationStatus;

public record VacationResponse(Long id, Long collaboratorId, String startDate, String endDate, VacationStatus status) {
}
