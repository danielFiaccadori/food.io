package net.dndats.foodio.infrastructure.repository;

import net.dndats.foodio.domain.model.Product;
import net.dndats.foodio.domain.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p.restaurant FROM Product p WHERE p.id = :id")
    Optional<Restaurant> findOwnerByProductId(Long id);

    @Query("SELECT p FROM Product p WHERE p.restaurant.uuid = :uuid")
    Page<Product> findAllByRestaurant(UUID uuid, Pageable pageable);

}
