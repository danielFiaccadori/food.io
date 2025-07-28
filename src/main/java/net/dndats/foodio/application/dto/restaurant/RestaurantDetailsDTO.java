package net.dndats.foodio.application.dto.restaurant;

import net.dndats.foodio.domain.model.Product;

import java.util.List;
import java.util.UUID;

public record RestaurantDetailsDTO(
        UUID uuid,
        String name,
        String description,
        String email,
        String imageUrl,
        Boolean isOpen,
        // Later, move to DTO
        List<Product> stock
) {}
