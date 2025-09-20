package com.taskflow.api.web.controller;

import com.taskflow.api.service.AuthorizationService;
import com.taskflow.api.web.dtos.AppResponse;
import com.taskflow.api.web.dtos.LoginRequest;
import com.taskflow.api.web.dtos.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class AuthController {

    private final AuthorizationService authorizationService;

        @Operation(
            summary = "User login",
            description = "Authenticates a user and returns a JWT token if credentials are valid."
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid login data",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid credentials",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppResponse.class)))
        })
    @PostMapping("/login")
    public ResponseEntity<AppResponse<TokenResponse>> login(@RequestBody @Valid LoginRequest request) {
        var response = authorizationService.login(request);
        return AppResponse.ok("Login successfull", response).getResponseEntity();
    }
}
