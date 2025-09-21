package com.taskflow.api.web.dtos;

import com.taskflow.api.domain.enums.Role;

public record TokenResponse(String token, Role role) {

}