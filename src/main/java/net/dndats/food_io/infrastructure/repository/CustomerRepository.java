package net.dndats.food_io.infrastructure.repository;

import net.dndats.food_io.domain.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
