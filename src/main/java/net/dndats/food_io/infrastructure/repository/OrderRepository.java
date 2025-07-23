package net.dndats.food_io.infrastructure.repository;

import net.dndats.food_io.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
