package net.dndats.foodio.application.dto.order;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDetailsDTO(
        Long id,
        String customerName,
        List<OrderItemDetailsDTO> products,
        LocalDateTime createdAt
) {}
