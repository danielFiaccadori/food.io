package net.dndats.foodio.application.service;

import net.dndats.foodio.adapters.mapper.ProductMapper;
import net.dndats.foodio.application.dto.order.OrderDetailsDTO;
import net.dndats.foodio.application.dto.product.CreateProductRequestDTO;
import net.dndats.foodio.application.dto.product.ProductDetailsDTO;
import net.dndats.foodio.application.dto.product.UpdateProductRequestDTO;
import net.dndats.foodio.domain.exception.ProductNotFoundException;
import net.dndats.foodio.domain.model.Product;
import net.dndats.foodio.domain.model.Restaurant;
import net.dndats.foodio.infrastructure.repository.ProductRepository;
import net.dndats.foodio.infrastructure.repository.RestaurantRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final RestaurantRepository restaurantRepository;
    private final ProductMapper mapper;

    public ProductService(ProductRepository repository, RestaurantRepository restaurantRepository, ProductMapper mapper) {
        this.repository = repository;
        this.restaurantRepository = restaurantRepository;
        this.mapper = mapper;
    }

    public ProductDetailsDTO create(CreateProductRequestDTO dto) {
        // Variation of canExecutePrivateAction
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Restaurant restaurant = restaurantRepository.findByEmail(email)
                .orElseThrow(() -> new AccessDeniedException("Authenticated restaurant not found."));

        Product product = new Product();
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setImageUrl(dto.imageUrl());
        product.setPrice(dto.price());
        product.setRestaurant(restaurant);

        return mapper.toDetailsDTO(repository.save(product));
    }

    public boolean update(Long toUpdateId, UpdateProductRequestDTO updateRequest) throws AccessDeniedException {
        Product product = repository.findById(toUpdateId)
                .orElseThrow(() -> new ProductNotFoundException(toUpdateId));
        if (!canExecutePrivateAction(toUpdateId)) return false;

        product.setName(updateRequest.name());
        product.setDescription(updateRequest.description());
        product.setImageUrl(updateRequest.imageUrl());
        product.setPrice(updateRequest.price());

        repository.save(product);
        return true;
    }

    public boolean delete(Long id) throws AccessDeniedException {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        if (!canExecutePrivateAction(id)) return false;

        repository.delete(product);
        return true;
    }

    /**
     * This method grants a user only can execute update/delete with their own products.
     */
    private boolean canExecutePrivateAction(Long productId) throws AccessDeniedException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Restaurant executor = restaurantRepository.findByEmail(email)
                .orElseThrow(() -> new AccessDeniedException("Authenticated restaurant not found."));

        Product product = repository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        if (!product.getRestaurant().getUuid().equals(executor.getUuid())) {
            throw new AccessDeniedException("You do not have permission to perform this action.");
        }

        return true;
    }


}
