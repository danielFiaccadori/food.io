package net.dndats.foodio.application.service;

import net.dndats.foodio.adapters.mapper.OrderMapper;
import net.dndats.foodio.application.dto.order.CreateOrderItemDTO;
import net.dndats.foodio.application.dto.order.CreateOrderRequestDTO;
import net.dndats.foodio.application.dto.order.OrderDetailsDTO;
import net.dndats.foodio.domain.exception.CustomerNotFoundException;
import net.dndats.foodio.domain.exception.ProductNotFoundException;
import net.dndats.foodio.domain.exception.RestaurantNotFoundException;
import net.dndats.foodio.domain.model.*;
import net.dndats.foodio.domain.model.pk.OrderEmbeddedId;
import net.dndats.foodio.infrastructure.repository.CustomerRepository;
import net.dndats.foodio.infrastructure.repository.OrderRepository;
import net.dndats.foodio.infrastructure.repository.ProductRepository;
import net.dndats.foodio.infrastructure.repository.RestaurantRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderMapper orderMapper;

    public OrderService(
            OrderRepository orderRepository,
            ProductRepository productRepository,
            CustomerRepository customerRepository,
            RestaurantRepository restaurantRepository, OrderMapper orderMapper
    ) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderMapper = orderMapper;
    }

    // Locate orders

    public List<OrderDetailsDTO> findOrdersForCustomer(UUID customerUUID) throws AccessDeniedException {
        Customer customer = customerRepository.findById(customerUUID)
                .orElseThrow(() -> new CustomerNotFoundException(customerUUID));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!customer.getEmail().equals(email)) {
            throw new AccessDeniedException("You do not have permission to view this customer's orders.");
        }

        List<Order> orders = orderRepository.findByCustomerUUID(customerUUID);
        return orders.stream().map(orderMapper::toDetailsDTO).toList();
    }

    public List<OrderDetailsDTO> findOrdersForRestaurant(UUID restaurantUUID) throws AccessDeniedException {
        Restaurant restaurant = restaurantRepository.findById(restaurantUUID)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantUUID));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!restaurant.getEmail().equals(email)) {
            throw new AccessDeniedException("You do not have permission to view this restaurant's orders.");
        }

        List<Order> orders = orderRepository.findByRestaurantUUID(restaurantUUID);
        return orders.stream().map(orderMapper::toDetailsDTO).toList();
    }

    // Create orders

    public boolean createOrder(CreateOrderRequestDTO createOrderRequest) {
        Customer customer = customerRepository.findById(createOrderRequest.customerUUID())
                .orElseThrow(() -> new CustomerNotFoundException(createOrderRequest.customerUUID()));
        Restaurant restaurant = restaurantRepository.findById(createOrderRequest.restaurantUUID())
                .orElseThrow(() -> new RestaurantNotFoundException(createOrderRequest.restaurantUUID()));

        Order order = new Order();
        order.setCustomer(customer);
        order.setRestaurant(restaurant);

        List<OrderItem> orderItems = new ArrayList<>();
        for (CreateOrderItemDTO item : createOrderRequest.products()) {
            Product product = productRepository.findById(item.productId())
                    .orElseThrow(() -> new ProductNotFoundException(item.productId()));

            OrderItem orderItem = new OrderItem();

            OrderEmbeddedId id = new OrderEmbeddedId();
            id.setOrderId(order.getId());
            id.setProductId(product.getId());

            orderItem.setId(id);
            orderItem.setProduct(product);
            orderItem.setOrder(order);
            orderItem.setQuantity(item.quantity());

            orderItems.add(orderItem);
        }

        order.setProducts(orderItems);
        orderRepository.save(order);

        return true;
    }

}
