package net.dndats.foodio.application.dto.restaurant;

public record RestaurantFinancialStatisticsDTO(
        Long count,
        Double sum,
        Double avg,
        Double max,
        Double min
) {}
