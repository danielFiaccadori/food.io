package net.dndats.foodio.infrastructure.repository;

import io.micrometer.common.lang.NonNull;
import net.dndats.foodio.domain.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    @NonNull
    Page<Customer> findAll(@NonNull Pageable pageable);
    Optional<Customer> findByEmail(String email);

}
