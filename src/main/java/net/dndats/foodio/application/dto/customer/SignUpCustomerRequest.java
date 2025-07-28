package net.dndats.foodio.application.dto.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import net.dndats.foodio.domain.Role;

public record SignUpCustomerRequest(
        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,

        @NotBlank(message = "Password is required")
        String password,

        @NotBlank(message = "Phone number is required")
        String phoneNumber,

        @NotBlank(message = "Address is required")
        String address,

        @NotNull(message = "Role is required")
        Role role
) {}