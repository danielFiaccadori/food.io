package net.dndats.food_io.infrastructure.repository;

import net.dndats.food_io.domain.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> {
}
