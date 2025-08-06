package net.dndats.foodio.interfaces;

import jakarta.validation.Valid;
import net.dndats.foodio.application.dto.order.OrderDetailsDTO;
import net.dndats.foodio.application.dto.restaurant.RestaurantDetailsDTO;
import net.dndats.foodio.application.dto.restaurant.RestaurantFinancialStatisticsDTO;
import net.dndats.foodio.application.dto.restaurant.RestaurantOrderStatisticsDTO;
import net.dndats.foodio.application.dto.restaurant.UpdateRestaurantRequestDTO;
import net.dndats.foodio.application.response.BaseResponse;
import net.dndats.foodio.application.service.OrderService;
import net.dndats.foodio.application.service.RestaurantService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {

    private final RestaurantService service;
    private final OrderService orderService;

    public RestaurantController(RestaurantService service, OrderService orderService) {
        this.service = service;
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<BaseResponse<Page<RestaurantDetailsDTO>>> findAll(Pageable pageable) {
        Page<RestaurantDetailsDTO> restaurants = service.findAll(pageable);
        return ResponseEntity.ok().body(BaseResponse.ok(restaurants));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<BaseResponse<RestaurantDetailsDTO>> findByUUID(@PathVariable UUID uuid) {
        RestaurantDetailsDTO restaurant = service.findById(uuid);
        return ResponseEntity.ok().body(BaseResponse.ok(restaurant));
    }

    @GetMapping("/self")
    public ResponseEntity<BaseResponse<RestaurantDetailsDTO>> findSelf() {
        RestaurantDetailsDTO restaurant = service.getAuthenticatedRestaurantDetails();
        return ResponseEntity.ok().body(BaseResponse.ok(restaurant));
    }

    @PreAuthorize("hasRole('RESTAURANT')")
    @PutMapping("/orders/accept")
    public ResponseEntity<BaseResponse<Boolean>> acceptOrder(@RequestParam Long orderId) throws AccessDeniedException {
        boolean success = orderService.acceptOrder(orderId);
        return ResponseEntity.ok().body(BaseResponse.ok(success));
    }

    @PreAuthorize("hasRole('RESTAURANT')")
    @PutMapping("/orders/reject")
    public ResponseEntity<BaseResponse<Boolean>> rejectOrder(@RequestParam Long orderId) throws AccessDeniedException {
        boolean success = orderService.rejectOrder(orderId);
        return ResponseEntity.ok().body(BaseResponse.ok(success));
    }

    @PreAuthorize("hasRole('RESTAURANT')")
    @GetMapping("/orders")
    public ResponseEntity<BaseResponse<Page<OrderDetailsDTO>>> getOrders(Pageable pageable) {
        Page<OrderDetailsDTO> orders = orderService.findOrdersForRestaurant(pageable);
        return ResponseEntity.ok().body(BaseResponse.ok(orders));
    }

    @PreAuthorize("hasRole('RESTAURANT')")
    @GetMapping("/financial/statistics")
    public ResponseEntity<BaseResponse<RestaurantFinancialStatisticsDTO>> getFinancialStatistics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime endDate
    ) {
        var statistics = service.getFinancialStatistics(startDate, endDate);
        return ResponseEntity.ok().body(BaseResponse.ok(statistics, "Financial statistics retrieved successfully"));
    }

    @PreAuthorize("hasRole('RESTAURANT')")
    @GetMapping("/orders/statistics")
    public ResponseEntity<BaseResponse<RestaurantOrderStatisticsDTO>> getOrderStatistics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime endDate
    ) {
        var statistics = service.getOrderStatistics(startDate, endDate);
        return ResponseEntity.ok().body(BaseResponse.ok(statistics, "Order statistics retrieved successfully"));
    }

    @PreAuthorize("hasRole('RESTAURANT')")
    @PutMapping("/update")
    public ResponseEntity<BaseResponse<Boolean>> update(
            @Valid UpdateRestaurantRequestDTO requestDTO) throws AccessDeniedException {
        boolean success = service.update(requestDTO);
        return ResponseEntity.ok().body(BaseResponse.ok(success, "Restaurant updated successfully"));
    }

    @PreAuthorize("hasRole('RESTAURANT')")
    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponse<Boolean>> delete() throws AccessDeniedException {
        boolean success = service.delete();
        return ResponseEntity.ok().body(BaseResponse.ok(success, "Restaurant deleted successfully"));
    }

}
