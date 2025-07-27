package net.dndats.foodio.application.dto.customer;

public record UpdateCustomerRequestDTO(
        String username,
        String address,
        String phoneNumber
) {}
