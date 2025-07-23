package net.dndats.food_io.domain.model;

import jakarta.persistence.*;
import net.dndats.food_io.domain.model.pk.OrderEmbeddedId;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @EmbeddedId
    private OrderEmbeddedId id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

}
