package net.dndats.foodio.application.dto.restaurant;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import net.dndats.foodio.domain.Role;

public record SignUpRestaurantRequest(
        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,

        @NotBlank(message = "Password is required")
        String password,

        @NotBlank(message = "Restaurant name is required")
        String name,

        String description,
        String imageUrl,

        boolean isOpen,

        @NotNull(message = "Role is required")
        Role role
) {}
