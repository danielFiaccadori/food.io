package net.dndats.food_io.domain.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> products;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}
