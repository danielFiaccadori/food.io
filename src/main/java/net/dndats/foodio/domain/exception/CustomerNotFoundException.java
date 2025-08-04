package net.dndats.foodio.domain.exception;

import java.util.UUID;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(UUID uuid) {
        super("Customer not found with UUID: " + uuid);
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
