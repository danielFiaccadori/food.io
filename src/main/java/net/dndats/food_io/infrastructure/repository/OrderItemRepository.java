package net.dndats.food_io.infrastructure.repository;

import net.dndats.food_io.domain.model.pk.OrderEmbeddedId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderEmbeddedId, Long> {
}
