package net.dndats.foodio.infrastructure.repository;

import net.dndats.foodio.application.dto.restaurant.RestaurantDetailsDTO;
import net.dndats.foodio.domain.model.Product;
import net.dndats.foodio.domain.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p.restaurant FROM Product p WHERE p.id = :id")
    Optional<Restaurant> findOwnerByProductId(Long id);

}
