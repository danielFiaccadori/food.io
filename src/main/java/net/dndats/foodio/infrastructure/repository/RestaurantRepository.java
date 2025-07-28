package net.dndats.foodio.infrastructure.repository;

import io.micrometer.common.lang.NonNull;
import net.dndats.foodio.domain.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {

    @NonNull
    Page<Restaurant> findAll(@NonNull Pageable pageable);
    Optional<Restaurant> findByEmail(String email);

}
