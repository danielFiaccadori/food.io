package net.dndats.foodio.application.dto.order;

public record OrderItemDetailsDTO(
        String productName,
        int quantity
) {}
