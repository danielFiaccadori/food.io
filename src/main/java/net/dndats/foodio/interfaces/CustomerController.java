package net.dndats.foodio.interfaces;

import jakarta.validation.Valid;
import net.dndats.foodio.application.dto.customer.CustomerDetailsDTO;
import net.dndats.foodio.application.dto.customer.UpdateCustomerRequestDTO;
import net.dndats.foodio.application.dto.order.CreateOrderRequestDTO;
import net.dndats.foodio.application.dto.order.OrderDetailsDTO;
import net.dndats.foodio.application.response.BaseResponse;
import net.dndats.foodio.application.service.CustomerService;
import net.dndats.foodio.application.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService service;
    private final OrderService orderService;

    public CustomerController(CustomerService service, OrderService orderService) {
        this.service = service;
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<CustomerDetailsDTO>>> findAll(Pageable pageable) {
        List<CustomerDetailsDTO> customers = service.findAll(pageable);
        return ResponseEntity.ok().body(BaseResponse.ok(customers));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<BaseResponse<CustomerDetailsDTO>> findByUUID(@PathVariable UUID uuid) {
        CustomerDetailsDTO customer = service.findById(uuid);
        return ResponseEntity.ok().body(BaseResponse.ok(customer));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/{uuid}/orders")
    public ResponseEntity<BaseResponse<List<OrderDetailsDTO>>> getOrders(@PathVariable UUID uuid) {
        List<OrderDetailsDTO> orders = orderService.findOrdersForCustomer(uuid);
        return ResponseEntity.ok().body(BaseResponse.ok(orders));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/order")
    public ResponseEntity<BaseResponse<Boolean>> createOrder(@Valid @RequestBody CreateOrderRequestDTO createOrderRequest) {
        boolean success = orderService.createOrder(createOrderRequest);
        return ResponseEntity.ok().body(BaseResponse.ok(success, "Order created successfully"));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/{uuid}")
    public ResponseEntity<BaseResponse<Boolean>> update(
            @PathVariable UUID uuid, @Valid @RequestBody UpdateCustomerRequestDTO requestDTO
    ) {
        boolean success = service.update(uuid, requestDTO);
        return ResponseEntity.ok().body(BaseResponse.ok(success, "Customer updated successfully"));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<BaseResponse<Boolean>> delete(@PathVariable UUID uuid) {
        boolean success = service.delete(uuid);
        return ResponseEntity.ok().body(BaseResponse.ok(success, "Customer deleted successfully"));
    }

}