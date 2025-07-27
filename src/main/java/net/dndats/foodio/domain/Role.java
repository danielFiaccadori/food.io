package net.dndats.foodio.domain;

import lombok.Getter;

@Getter
public enum Role {

    CUSTOMER("CUSTOMER"),
    RESTAURANT("RESTAURANT");

    private final String description;

    Role(String description) {
        this.description = description;
    }

}
