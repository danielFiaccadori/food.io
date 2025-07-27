package net.dndats.foodio.application.dto.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import net.dndats.foodio.domain.Role;

public record SignUpRequest(
        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,

        @NotBlank(message = "Password is required")
        String password,

        @NotBlank(message = "Phone number is required")
        String phoneNumber,

        @NotBlank(message = "Choose an account type")
        Role role,

        String address
) {}
