package net.dndats.food_io.domain.exception;

import java.util.UUID;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(UUID uuid) {
        super("Customer not found with UUID: " + uuid);
    }}
