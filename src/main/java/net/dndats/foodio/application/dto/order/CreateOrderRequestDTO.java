package net.dndats.foodio.application.dto.order;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequestDTO(
        @NotNull(message = "Customer id cannot be null")
        UUID customerUUID,

        @NotNull(message = "Restaurant id cannot be null")
        UUID restaurantUUID,

        List<CreateOrderItemDTO> products
) {}
