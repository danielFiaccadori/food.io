package net.dndats.food_io.application.service.customer;

import net.dndats.food_io.adapters.mapper.CustomerMapper;
import net.dndats.food_io.application.dto.customer.CustomerDetailsDTO;
import net.dndats.food_io.application.dto.customer.SignUpCustomerRequestDTO;
import net.dndats.food_io.application.dto.customer.UpdateCustomerRequestDTO;
import net.dndats.food_io.domain.exception.CustomerNotFoundException;
import net.dndats.food_io.domain.model.Customer;
import net.dndats.food_io.infrastructure.repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<CustomerDetailsDTO> findAll(Pageable pageable) {
        return customerMapper.toCustomerDetailsDTOList(customerRepository.findAll(pageable));
    }

    public CustomerDetailsDTO findById(UUID uuid) {
        Customer customer = customerRepository.findById(uuid)
                .orElseThrow(() -> new CustomerNotFoundException(uuid));
        return customerMapper.toCustomerDetailsDTO(customer);
    }

    public CustomerDetailsDTO register(SignUpCustomerRequestDTO signUpRequestDTO) {
        Customer toRegisterUse = customerMapper.toCustomer(signUpRequestDTO);

        // Generates an encoded password
        String passwordEncoded = passwordEncoder.encode(signUpRequestDTO.password());
        toRegisterUse.setPassword(passwordEncoded);

        Customer toSaveCustomer = customerRepository.save(toRegisterUse);
        return customerMapper.toCustomerDetailsDTO(toSaveCustomer);
    }

    public CustomerDetailsDTO update(UUID toUpdateUUID, UpdateCustomerRequestDTO updateRequestDTO) {
        Customer toUpdateCustomer = customerRepository.findById(toUpdateUUID)
                .orElseThrow(() -> new CustomerNotFoundException(toUpdateUUID));

        toUpdateCustomer.setAddress(updateRequestDTO.address());
        toUpdateCustomer.setUsername(updateRequestDTO.username());
        toUpdateCustomer.setPhoneNumber(updateRequestDTO.phoneNumber());

        Customer savedCustomer = customerRepository.save(toUpdateCustomer);
        return customerMapper.toCustomerDetailsDTO(savedCustomer);
    }

    public boolean delete(UUID uuid) {
        Customer customer = customerRepository.findById(uuid)
                .orElseThrow(() -> new CustomerNotFoundException(uuid));
        boolean success = customer != null;
        if (success) customerRepository.delete(customer);

        // Return true if the customer was found and deleted, false otherwise
        return success;
    }

}
