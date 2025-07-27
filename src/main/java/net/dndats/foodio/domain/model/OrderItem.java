package net.dndats.foodio.domain.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.dndats.foodio.domain.model.pk.OrderEmbeddedId;

@Entity
@Getter
@Setter
@EqualsAndHashCode
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
