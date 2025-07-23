package net.dndats.food_io.infrastructure.repository;

import net.dndats.food_io.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
