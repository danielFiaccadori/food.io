package net.dndats.foodio.interfaces;

import jakarta.validation.Valid;
import net.dndats.foodio.application.dto.restaurant.RestaurantDetailsDTO;
import net.dndats.foodio.application.dto.restaurant.UpdateRestaurantRequestDTO;
import net.dndats.foodio.application.response.BaseResponse;
import net.dndats.foodio.application.service.RestaurantService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {

    private final RestaurantService service;

    public RestaurantController(RestaurantService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<RestaurantDetailsDTO>>> findAll(Pageable pageable) {
        List<RestaurantDetailsDTO> restaurants = service.findAll(pageable);
        return ResponseEntity.ok().body(BaseResponse.ok(restaurants));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<BaseResponse<RestaurantDetailsDTO>> findByUUID(@PathVariable UUID uuid) {
        RestaurantDetailsDTO restaurant = service.findById(uuid);
        return ResponseEntity.ok().body(BaseResponse.ok(restaurant));
    }

    @PreAuthorize("hasRole('RESTAURANT')")
    @PutMapping("/{uuid}")
    public ResponseEntity<BaseResponse<Boolean>> update(
            @PathVariable UUID uuid, @Valid UpdateRestaurantRequestDTO requestDTO) throws AccessDeniedException {
        boolean success = service.update(uuid, requestDTO);
        return ResponseEntity.ok().body(BaseResponse.ok(success, "Restaurant updated successfully"));
    }

    @PreAuthorize("hasRole('RESTAURANT')")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<BaseResponse<Boolean>> delete(@PathVariable UUID uuid) throws AccessDeniedException {
        boolean success = service.delete(uuid);
        return ResponseEntity.ok().body(BaseResponse.ok(success, "Restaurant deleted successfully"));
    }

}
