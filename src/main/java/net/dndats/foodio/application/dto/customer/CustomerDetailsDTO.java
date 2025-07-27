package net.dndats.foodio.application.dto.customer;

import java.util.UUID;

public record CustomerDetailsDTO(
        UUID uuid,
        String username,
        String email,
        String phoneNumber,
        String address
) {}
