package net.dndats.foodio.interfaces;

import net.dndats.foodio.application.dto.customer.CustomerDetailsDTO;
import net.dndats.foodio.application.dto.security.AuthenticationResponse;
import net.dndats.foodio.application.dto.security.LoginRequest;
import net.dndats.foodio.application.dto.security.SignUpRequest;
import net.dndats.foodio.application.response.BaseResponse;
import net.dndats.foodio.application.service.customer.CustomerService;
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

    public AuthenticationController(AuthenticationManager authManager, JwtService jwtService, CustomerService customerService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<BaseResponse<AuthenticationResponse>> register(@RequestBody SignUpRequest request) {
        switch (request.role()) {
            case CUSTOMER -> {
                CustomerDetailsDTO details = customerService.register(request);
            }
            case RESTAURANT -> {
                // TODO: Implement restaurant registration logic
            }
        }

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        UserDetails details = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(details);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.ok(new AuthenticationResponse("Successfully registered! ", token)));
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
