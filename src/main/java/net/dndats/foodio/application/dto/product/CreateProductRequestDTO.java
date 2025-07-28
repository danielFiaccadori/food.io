package net.dndats.foodio.application.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateProductRequestDTO(
        @NotBlank(message = "Product name is required")
        String name,

        String description,
        String imageUrl,

        @NotNull @Positive(message = "Price must be a positive number")
        BigDecimal price
) {
}
