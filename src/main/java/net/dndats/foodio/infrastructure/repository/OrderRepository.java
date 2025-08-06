package net.dndats.foodio.infrastructure.repository;

import net.dndats.foodio.domain.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.customer.uuid = :customerUUID")
    Page<Order> findByCustomerUUID(UUID customerUUID, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.restaurant.uuid = :restaurantUUID")
    Page<Order> findByRestaurantUUID(UUID restaurantUUID, Pageable pageable);

}
