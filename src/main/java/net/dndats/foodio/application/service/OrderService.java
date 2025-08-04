package net.dndats.foodio.application.service;

import net.dndats.foodio.adapters.mapper.OrderMapper;
import net.dndats.foodio.application.dto.order.CreateOrderItemDTO;
import net.dndats.foodio.application.dto.order.CreateOrderRequestDTO;
import net.dndats.foodio.application.dto.order.OrderDetailsDTO;
import net.dndats.foodio.domain.exception.CustomerNotFoundException;
import net.dndats.foodio.domain.exception.OrderNotFoundException;
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

    // Operations

    public boolean rejectOrder(Long orderId) throws AccessDeniedException{
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        Restaurant restaurant = restaurantRepository.findByEmail(order.getRestaurant().getEmail())
                .orElseThrow(() -> new RestaurantNotFoundException("Authenticated restaurant not found!"));

        if (!order.getRestaurant().getUuid().equals(restaurant.getUuid())) {
            throw new AccessDeniedException("You do not have permission to reject this order.");
        }

        order.setOrderStatus(OrderStatus.REJECTED);
        orderRepository.save(order);

        return true;
    }

    public boolean acceptOrder(Long orderId) throws AccessDeniedException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        Restaurant restaurant = restaurantRepository.findByEmail(order.getRestaurant().getEmail())
                .orElseThrow(() -> new RestaurantNotFoundException("Authenticated restaurant not found!"));

        if (!order.getRestaurant().getUuid().equals(restaurant.getUuid())) {
            throw new AccessDeniedException("You do not have permission to accept this order.");
        }

        order.setOrderStatus(OrderStatus.ACCEPTED);
        orderRepository.save(order);

        return true;
    }

    // Locate orders

    public List<OrderDetailsDTO> findOrdersForCustomer() throws AccessDeniedException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException("Authenticated customer not found!"));

        List<Order> orders = orderRepository.findByCustomerUUID(customer.getUuid());
        return orders.stream().map(orderMapper::toDetailsDTO).toList();
    }

    public List<OrderDetailsDTO> findOrdersForRestaurant() throws AccessDeniedException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Restaurant restaurant = restaurantRepository.findByEmail(email)
                .orElseThrow(() -> new RestaurantNotFoundException("Authenticated restaurant not found!"));

        List<Order> orders = orderRepository.findByRestaurantUUID(restaurant.getUuid());
        return orders.stream().map(orderMapper::toDetailsDTO).toList();
    }

    // Create orders

    public boolean createOrder(CreateOrderRequestDTO createOrderRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Customer customer = customerRepository.findById(createOrderRequest.customerUUID())
                .orElseThrow(() -> new CustomerNotFoundException(createOrderRequest.customerUUID()));
        Restaurant restaurant = restaurantRepository.findById(createOrderRequest.restaurantUUID())
                .orElseThrow(() -> new RestaurantNotFoundException(createOrderRequest.restaurantUUID()));

        if (!customer.getUuid().equals(createOrderRequest.customerUUID())) {
            throw new AccessDeniedException("You do not have permission to create this order.");
        }

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
