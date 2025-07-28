package net.dndats.foodio.infrastructure.security;

import net.dndats.foodio.domain.model.Customer;
import net.dndats.foodio.domain.model.Restaurant;
import net.dndats.foodio.infrastructure.repository.CustomerRepository;
import net.dndats.foodio.infrastructure.repository.RestaurantRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;

    public UserDetailsServiceImpl(CustomerRepository customerRepository, RestaurantRepository restaurantRepository) {
        this.customerRepository = customerRepository;
         this.restaurantRepository = restaurantRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            return new User(
                    customer.getEmail(),
                    customer.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_CUSTOMER"))
            );
        }

         Optional<Restaurant> restaurantOpt = restaurantRepository.findByEmail(email);
         if (restaurantOpt.isPresent()) {
             Restaurant restaurant = restaurantOpt.get();
             return new User(
                 restaurant.getEmail(),
                 restaurant.getPassword(),
                 Collections.singletonList(new SimpleGrantedAuthority("ROLE_RESTAURANT"))
             );
         }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }

}
