package com.taskflow.api.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum VacationStatus {

    PENDING("pending"),
    APPROVED("approved"),
    DENIED("denied"),
    CANCELLED("cancelled");

    private String description;
}
