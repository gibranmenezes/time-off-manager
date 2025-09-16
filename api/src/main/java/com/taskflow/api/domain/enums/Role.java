package com.taskflow.api.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

    ADMIN("admin"),
    COLLABORATOR("collaborator"),
    MANAGER("manager");

    private String description;
}
