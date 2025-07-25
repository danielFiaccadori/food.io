package net.dndats.food_io.infrastructure.repository;

import net.dndats.food_io.domain.model.OrderItems;
import net.dndats.food_io.domain.model.pk.OrderEmbeddedId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItems, OrderEmbeddedId> {
}
