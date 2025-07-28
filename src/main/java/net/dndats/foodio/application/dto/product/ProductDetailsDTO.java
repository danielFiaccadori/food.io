package net.dndats.foodio.application.dto.product;

import java.math.BigDecimal;

public record ProductDetailsDTO(
       Long id,
       String name,
       String description,
       String imageUrl,
       BigDecimal price
) {}
