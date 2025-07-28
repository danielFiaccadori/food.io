package net.dndats.foodio.interfaces;

import jakarta.validation.Valid;
import net.dndats.foodio.application.dto.customer.CustomerDetailsDTO;
import net.dndats.foodio.application.dto.customer.UpdateCustomerRequestDTO;
import net.dndats.foodio.application.response.BaseResponse;
import net.dndats.foodio.application.service.CustomerService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
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
    @PutMapping("/{uuid}")
    public ResponseEntity<BaseResponse<Boolean>> update(
            @PathVariable UUID uuid, @Valid @RequestBody UpdateCustomerRequestDTO requestDTO
    ) throws AccessDeniedException {
        boolean success = service.update(uuid, requestDTO);
        return ResponseEntity.ok().body(BaseResponse.ok(success, "Customer updated successfully"));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<BaseResponse<Boolean>> delete(@PathVariable UUID uuid) throws AccessDeniedException {
        boolean success = service.delete(uuid);
        return ResponseEntity.ok().body(BaseResponse.ok(success, "Customer deleted successfully"));
    }

}