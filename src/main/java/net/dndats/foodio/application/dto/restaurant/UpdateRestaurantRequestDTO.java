package net.dndats.foodio.application.dto.restaurant;

public record UpdateRestaurantRequestDTO(
        String name,
        String description,
        String imageUrl,
        Boolean isOpen
) {}
