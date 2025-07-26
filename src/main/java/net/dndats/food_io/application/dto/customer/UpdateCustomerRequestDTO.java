package net.dndats.food_io.application.dto.customer;

public record UpdateCustomerRequestDTO(
        String username,
        String address,
        String phoneNumber
) {}
