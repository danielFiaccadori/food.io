package net.dndats.foodio.infrastructure.repository;

import net.dndats.foodio.domain.model.OrderItem;
import net.dndats.foodio.domain.model.pk.OrderEmbeddedId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItem, OrderEmbeddedId> {
}
