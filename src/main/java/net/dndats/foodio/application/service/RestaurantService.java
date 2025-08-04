package net.dndats.foodio.application.service;

import net.dndats.foodio.adapters.mapper.ProductMapper;
import net.dndats.foodio.adapters.mapper.RestaurantMapper;
import net.dndats.foodio.application.dto.product.ProductDetailsDTO;
import net.dndats.foodio.application.dto.restaurant.RestaurantDetailsDTO;
import net.dndats.foodio.application.dto.restaurant.RestaurantFinancialStatisticsDTO;
import net.dndats.foodio.application.dto.restaurant.SignUpRestaurantRequest;
import net.dndats.foodio.application.dto.restaurant.UpdateRestaurantRequestDTO;
import net.dndats.foodio.domain.exception.RestaurantNotFoundException;
import net.dndats.foodio.domain.model.Order;
import net.dndats.foodio.domain.model.OrderItem;
import net.dndats.foodio.domain.model.Product;
import net.dndats.foodio.domain.model.Restaurant;
import net.dndats.foodio.infrastructure.repository.ProductRepository;
import net.dndats.foodio.infrastructure.repository.RestaurantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class RestaurantService {

    private final RestaurantRepository repository;
    private final ProductRepository productRepository;
    private final RestaurantMapper mapper;
    private final ProductMapper productMapper;
    private final PasswordEncoder passwordEncoder;

    public RestaurantService(RestaurantRepository repository, ProductRepository productRepository, RestaurantMapper mapper, ProductMapper productMapper, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.productRepository = productRepository;
        this.mapper = mapper;
        this.productMapper = productMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // Operations

    public RestaurantFinancialStatisticsDTO getStatistics(LocalDateTime start, LocalDateTime end) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Restaurant restaurant = repository.findByEmail(email)
                .orElseThrow(() -> new RestaurantNotFoundException("Authenticated restaurant not found!"));

        List<Order> orders = restaurant.getOrders();

        Stream<OrderItem> filteredItems = orders.stream()
                .filter(order -> {
                    LocalDateTime createdAt = order.getCreatedAt();
                    return (start == null || !createdAt.isBefore(start)) &&
                            (end == null || !createdAt.isAfter(end));
                }).flatMap(order -> order.getProducts().stream());

        DoubleSummaryStatistics statistics = filteredItems
                .map(OrderItem::getProduct)
                .mapToDouble(p -> p.getPrice().doubleValue())
                .summaryStatistics();

        if (statistics.getCount() == 0) {
            return new RestaurantFinancialStatisticsDTO(0L, 0.0, 0.0, 0.0, 0.0);
        }

        return new RestaurantFinancialStatisticsDTO(
                statistics.getCount(),
                statistics.getSum(),
                statistics.getAverage(),
                statistics.getMax(),
                statistics.getMin()
        );
    }

    public List<ProductDetailsDTO> findAllProductsForRestaurant(Pageable pageable) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Restaurant restaurant = repository.findByEmail(email)
                .orElseThrow(() -> new RestaurantNotFoundException("Authenticated restaurant not found!"));

        Page<Product> page = productRepository.findAllByRestaurant(restaurant.getUuid(), pageable);
        return page.stream()
                .map(productMapper::toDetailsDTO).toList();
    }

    // CRUD

    public List<RestaurantDetailsDTO> findAll(Pageable pageable) {
        Page<Restaurant> page = repository.findAll(pageable);
        return page.stream().map(mapper::toDetailsDTO).toList();
    }

    public RestaurantDetailsDTO findById(UUID uuid) {
        Restaurant restaurant = repository.findById(uuid)
                .orElseThrow(() -> new RestaurantNotFoundException(uuid));
        return mapper.toDetailsDTO(restaurant);
    }

    public void register(SignUpRestaurantRequest request) {
        Restaurant toRegisterRestaurant = mapper.toRestaurantSignUpRequest(request);

        // Generates an encoded password
        String passwordEncoded = passwordEncoder.encode(request.password());
        toRegisterRestaurant.setPassword(passwordEncoded);

        Restaurant toSave = repository.save(toRegisterRestaurant);
        mapper.toDetailsDTO(toSave);
    }

    public boolean update(UpdateRestaurantRequestDTO updateRequestDTO) throws AccessDeniedException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Restaurant toUpdateRestaurant = repository.findByEmail(email)
                .orElseThrow(() -> new RestaurantNotFoundException("Authenticated restaurant not found!"));

        toUpdateRestaurant.setName(updateRequestDTO.name());
        toUpdateRestaurant.setDescription(updateRequestDTO.description());
        toUpdateRestaurant.setImageUrl(updateRequestDTO.imageUrl());
        toUpdateRestaurant.setOpen(updateRequestDTO.isOpen());

        repository.save(toUpdateRestaurant);
        return true;
    }

    public boolean delete() throws AccessDeniedException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Restaurant toDeleteRestaurant = repository.findByEmail(email)
                .orElseThrow(() -> new RestaurantNotFoundException("Authenticated restaurant not found!"));

        repository.delete(toDeleteRestaurant);
        return true;
    }

}
