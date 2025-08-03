package net.dndats.foodio.interfaces;

import jakarta.validation.Valid;
import net.dndats.foodio.application.dto.customer.SignUpCustomerRequest;
import net.dndats.foodio.application.dto.restaurant.SignUpRestaurantRequest;
import net.dndats.foodio.application.dto.security.AuthenticationResponse;
import net.dndats.foodio.application.dto.security.LoginRequest;
import net.dndats.foodio.application.response.BaseResponse;
import net.dndats.foodio.application.service.CustomerService;
import net.dndats.foodio.application.service.RestaurantService;
import net.dndats.foodio.infrastructure.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    // Login/SignUp dependencies
    private final CustomerService customerService;
    private final RestaurantService restaurantService;

    public AuthenticationController(AuthenticationManager authManager, JwtService jwtService, CustomerService customerService, RestaurantService restaurantService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.customerService = customerService;
        this.restaurantService = restaurantService;
    }

    @PostMapping("/register/customer")
    public ResponseEntity<BaseResponse<String>> registerCustomer(@RequestBody @Valid SignUpCustomerRequest request) {
        customerService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.ok("Customer registered successfully"));
    }

    @PostMapping("/register/restaurant")
    public ResponseEntity<BaseResponse<String>> registerRestaurant(@RequestBody @Valid SignUpRestaurantRequest request) {
        restaurantService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.ok("Restaurant registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<AuthenticationResponse>> login(@RequestBody LoginRequest request) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        UserDetails details = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(details);
        return ResponseEntity.ok().body(BaseResponse.ok(new AuthenticationResponse("Successfully logged in! ", token)));
    }

}
