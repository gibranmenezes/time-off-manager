package com.taskflow.api.domain.collaborator;

import com.taskflow.api.domain.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CollaboratorCreationRequest(@NotBlank @NotNull String name,
                                          @NotBlank @NotNull String username,
                                          @Email String email,
                                          @Pattern(regexp = "^(?=.{6,}).*$", message = "Password must be at least 6 characters")
                                          String password,
                                          @NotNull Role role,
                                          String department, Long managerId) {
}
