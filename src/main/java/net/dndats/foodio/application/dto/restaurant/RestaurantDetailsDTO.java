package net.dndats.foodio.application.dto.restaurant;

import net.dndats.foodio.application.dto.order.OrderDetailsDTO;
import net.dndats.foodio.application.dto.product.ProductDetailsDTO;

import java.util.List;
import java.util.UUID;

public record RestaurantDetailsDTO(
        UUID uuid,
        String name,
        String description,
        String email,
        String imageUrl,
        Boolean isOpen,
        List<ProductDetailsDTO> stock,
        List<OrderDetailsDTO> orders
) {}
