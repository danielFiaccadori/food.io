package net.dndats.foodio.application.dto.restaurant;

public record RestaurantOrderStatisticsDTO(
        Long totalOrders,
        Long acceptedOrders,
        Long rejectedOrders,
        Long pendingOrders
) {}
