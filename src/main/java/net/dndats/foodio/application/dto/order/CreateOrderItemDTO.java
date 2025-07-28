package net.dndats.foodio.application.dto.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateOrderItemDTO(
        @NotNull
        Long productId,

        @NotNull @Positive
        int quantity
) {}
