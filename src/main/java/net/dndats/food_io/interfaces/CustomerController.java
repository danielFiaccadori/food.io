package net.dndats.food_io.interfaces;

import jakarta.validation.Valid;
import net.dndats.food_io.application.dto.customer.CustomerDetailsDTO;
import net.dndats.food_io.application.dto.customer.SignUpCustomerRequestDTO;
import net.dndats.food_io.application.dto.customer.UpdateCustomerRequestDTO;
import net.dndats.food_io.application.response.BaseResponse;
import net.dndats.food_io.application.service.customer.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
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

    @PostMapping
    public ResponseEntity<BaseResponse<CustomerDetailsDTO>> register(
            @Valid @RequestBody SignUpCustomerRequestDTO signUpRequest
    ) {
        CustomerDetailsDTO registeredCustomer = service.register(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.ok(registeredCustomer, "Customer registered successfully"));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<BaseResponse<CustomerDetailsDTO>> update(
            @PathVariable UUID uuid, @Valid @RequestBody UpdateCustomerRequestDTO requestDTO
    ) {
        CustomerDetailsDTO updatedCustomer = service.update(uuid, requestDTO);
        return ResponseEntity.ok().body(BaseResponse.ok(updatedCustomer, "Customer updated successfully"));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<BaseResponse<Boolean>> delete(@PathVariable UUID uuid) {
        service.delete(uuid);
        return ResponseEntity.ok().body(BaseResponse.ok(true, "Customer deleted successfully"));
    }

}
