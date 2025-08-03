package net.dndats.foodio.domain.model;

import lombok.Getter;

@Getter
public enum OrderStatus {

    PENDING("Pending"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered"),
    ACCEPTED("Accepted"),
    CANCELLED("Cancelled"),
    REJECTED("Rejected");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

}
