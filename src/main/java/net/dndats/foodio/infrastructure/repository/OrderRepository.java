package net.dndats.foodio.infrastructure.repository;

import net.dndats.foodio.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.customer.uuid = :customerUUID")
    List<Order> findByCustomerUUID(UUID customerUUID);

    @Query("SELECT o FROM Order o WHERE o.restaurant.uuid = :restaurantUUID")
    List<Order> findByRestaurantUUID(UUID restaurantUUID);

}
