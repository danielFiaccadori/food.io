package net.dndats.foodio.interfaces;

import jakarta.validation.Valid;
import net.dndats.foodio.application.dto.product.CreateProductRequestDTO;
import net.dndats.foodio.application.dto.product.ProductDetailsDTO;
import net.dndats.foodio.application.dto.product.UpdateProductRequestDTO;
import net.dndats.foodio.application.response.BaseResponse;
import net.dndats.foodio.application.service.ProductService;
import net.dndats.foodio.application.service.RestaurantService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants/products")
@PreAuthorize("hasRole('RESTAURANT')")
public class ProductController {

    private final ProductService service;
    private final RestaurantService restaurantService;

    public ProductController(ProductService service, RestaurantService restaurantService) {
        this.service = service;
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<ProductDetailsDTO>>> findAllProducts(Pageable pageable) {
        List<ProductDetailsDTO> products = restaurantService.findAllProductsForRestaurant(pageable);
        return ResponseEntity.ok().body(BaseResponse.ok(products));
    }

    @PostMapping
    public ResponseEntity<BaseResponse<ProductDetailsDTO>> addProduct(@RequestBody @Valid CreateProductRequestDTO requestDTO) {
        ProductDetailsDTO created = service.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.ok(created, "Product added successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<Boolean>> updateProduct(
            @PathVariable Long id, @RequestBody UpdateProductRequestDTO updateRequest) {
        boolean success = service.update(id, updateRequest);
        return ResponseEntity.ok().body(BaseResponse.ok(success, "Product updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Boolean>> deleteProduct(@PathVariable Long id) {
        boolean success = service.delete(id);
        return ResponseEntity.ok().body(BaseResponse.ok(success, "Product deleted successfully"));
    }

}
