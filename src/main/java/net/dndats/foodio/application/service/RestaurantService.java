package net.dndats.foodio.application.service;

import net.dndats.foodio.adapters.mapper.RestaurantMapper;
import net.dndats.foodio.application.dto.restaurant.RestaurantDetailsDTO;
import net.dndats.foodio.application.dto.restaurant.SignUpRestaurantRequest;
import net.dndats.foodio.application.dto.restaurant.UpdateRestaurantRequestDTO;
import net.dndats.foodio.domain.exception.CustomerNotFoundException;
import net.dndats.foodio.domain.exception.RestaurantNotFoundException;
import net.dndats.foodio.domain.model.Restaurant;
import net.dndats.foodio.infrastructure.repository.RestaurantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RestaurantService {

    private final RestaurantRepository repository;
    private final RestaurantMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public RestaurantService(RestaurantRepository repository, RestaurantMapper mapper, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<RestaurantDetailsDTO> findAll(Pageable pageable) {
        Page<Restaurant> page = repository.findAll(pageable);
        return page.stream().map(mapper::toDetailsDTO).toList();
    }

    public RestaurantDetailsDTO findById(UUID uuid) {
        Restaurant restaurant = repository.findById(uuid)
                .orElseThrow(() -> new RestaurantNotFoundException(uuid));
        return mapper.toDetailsDTO(restaurant);
    }

    public RestaurantDetailsDTO register(SignUpRestaurantRequest request) {
        Restaurant toRegisterRestaurant = mapper.toRestaurantSignUpRequest(request);

        // Generates an encoded password
        String passwordEncoded = passwordEncoder.encode(request.password());
        toRegisterRestaurant.setPassword(passwordEncoded);

        Restaurant toSave = repository.save(toRegisterRestaurant);
        return mapper.toDetailsDTO(toSave);
    }

    public boolean update(UUID toUpdateUUID, UpdateRestaurantRequestDTO updateRequestDTO) throws AccessDeniedException {
        Restaurant toUpdateRestaurant = repository.findById(toUpdateUUID)
                .orElseThrow(() -> new RestaurantNotFoundException(toUpdateUUID));
        if (!canExecutePrivateAction(toUpdateUUID)) return false;

        toUpdateRestaurant.setName(updateRequestDTO.name());
        toUpdateRestaurant.setDescription(updateRequestDTO.description());
        toUpdateRestaurant.setImageUrl(updateRequestDTO.imageUrl());
        toUpdateRestaurant.setOpen(updateRequestDTO.isOpen());

        repository.save(toUpdateRestaurant);
        return true;
    }

    public boolean delete(UUID uuid) throws AccessDeniedException {
        Restaurant restaurant = repository.findById(uuid)
                .orElseThrow(() -> new CustomerNotFoundException(uuid));
        if (!canExecutePrivateAction(uuid)) return false;

        repository.delete(restaurant);
        return true;
    }

    /**
     * This method grants a user only can execute update/delete actions with its own profile
     */
    private boolean canExecutePrivateAction(UUID uuid) throws AccessDeniedException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Restaurant toVerifyIfCanExecute = repository.findByEmail(email)
                .orElseThrow(() -> new RestaurantNotFoundException(uuid));

        if (!toVerifyIfCanExecute.getUuid().equals(uuid)) {
            throw new AccessDeniedException("You do not have permission to perform this action.");
        }

        return true;
    }

}
