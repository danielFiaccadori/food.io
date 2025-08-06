package net.dndats.foodio.application.dto.order;

import net.dndats.foodio.application.dto.customer.CustomerDetailsDTO;
import net.dndats.foodio.domain.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDetailsDTO(
        Long id,
        CustomerDetailsDTO customer,
        List<OrderItemDetailsDTO> products,
        Double totalPrice,
        LocalDateTime createdAt,
        OrderStatus orderStatus
) {}
