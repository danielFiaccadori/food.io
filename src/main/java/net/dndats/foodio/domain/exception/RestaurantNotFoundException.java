package net.dndats.foodio.domain.exception;

import java.util.UUID;

public class RestaurantNotFoundException extends RuntimeException {
    public RestaurantNotFoundException(UUID uuid) {
        super("Restaurant not found with UUID: " + uuid);
    }}
