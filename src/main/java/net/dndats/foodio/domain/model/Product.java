package net.dndats.foodio.domain.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "products")
public class Product {

    @Id @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 100)
    private String description;

    @ManyToOne
    @JoinColumn(name = "restaurant_uuid")
    private Restaurant restaurant;

    @Column(name = "image_url", nullable = true)
    private String imageUrl;

    @Column(nullable = false)
    private BigDecimal price;

}
