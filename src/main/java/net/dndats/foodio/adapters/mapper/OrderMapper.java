package net.dndats.foodio.adapters.mapper;

import net.dndats.foodio.application.dto.customer.CustomerDetailsDTO;
import net.dndats.foodio.application.dto.order.OrderDetailsDTO;
import net.dndats.foodio.application.dto.order.OrderItemDetailsDTO;
import net.dndats.foodio.domain.model.Customer;
import net.dndats.foodio.domain.model.Order;
import net.dndats.foodio.domain.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "totalPrice", expression = "java(calculateTotalPrice(order))")
    OrderDetailsDTO toDetailsDTO(Order order);

    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "price", source = "product.price")
    OrderItemDetailsDTO toOrderItemDetailsDTO(OrderItem item);

    CustomerDetailsDTO toCustomerDetailsDTO(Customer customer);

    default double calculateTotalPrice(Order order) {
        if (order.getProducts() == null) return 0.0;

        return order.getProducts().stream()
                .mapToDouble(item -> item.getProduct().getPrice().doubleValue() * item.getQuantity())
                .sum();
    }

}
