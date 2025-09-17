package com.taskflow.api.domain.collaborator;

import com.taskflow.api.domain.enums.Role;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

@Valid
public record CollaboratorCreationRequest(@NotBlank @NotNull String name,
                                          @NotBlank @NotNull String username,
                                          @Email String email,
                                          @Pattern(regexp = "^(?=.{8,}).*$", message = "A senha deve ter pelo menos 8 caracteres")
                                      String password,
                                          @NotBlank @NotNull Role role,
                                          String department, UUID managerId) {
}
