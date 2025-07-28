package net.dndats.foodio.domain.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "restaurants")
public class Restaurant {

    @Id @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false, length = 50)
    private String name;

    @Column
    private String description;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "is_open", nullable = false)
    private boolean isOpen;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> stock = new ArrayList<>();

}
