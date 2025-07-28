package net.dndats.foodio.application.service;

import net.dndats.foodio.adapters.mapper.CustomerMapper;
import net.dndats.foodio.application.dto.customer.CustomerDetailsDTO;
import net.dndats.foodio.application.dto.customer.SignUpCustomerRequest;
import net.dndats.foodio.application.dto.customer.UpdateCustomerRequestDTO;
import net.dndats.foodio.domain.exception.CustomerNotFoundException;
import net.dndats.foodio.domain.model.Customer;
import net.dndats.foodio.infrastructure.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository repository, CustomerMapper mapper, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<CustomerDetailsDTO> findAll(Pageable pageable) {
        Page<Customer> page = repository.findAll(pageable);
        return page.stream().map(mapper::toCustomerDetailsDTO).toList();
    }

    public CustomerDetailsDTO findById(UUID uuid) {
        Customer customer = repository.findById(uuid)
                .orElseThrow(() -> new CustomerNotFoundException(uuid));
        return mapper.toCustomerDetailsDTO(customer);
    }

    public void register(SignUpCustomerRequest request) {
        Customer toRegisterUse = mapper.toCustomerSignUpRequest(request);

        // Generates an encoded password
        String passwordEncoded = passwordEncoder.encode(request.password());
        toRegisterUse.setPassword(passwordEncoded);

        Customer toSaveCustomer = repository.save(toRegisterUse);
        mapper.toCustomerDetailsDTO(toSaveCustomer);
    }

    public boolean update(UUID toUpdateUUID, UpdateCustomerRequestDTO updateRequestDTO) throws AccessDeniedException {
        Customer toUpdateCustomer = repository.findById(toUpdateUUID)
                .orElseThrow(() -> new CustomerNotFoundException(toUpdateUUID));
        if (!canExecutePrivateAction(toUpdateUUID)) return false;

        toUpdateCustomer.setAddress(updateRequestDTO.address());
        toUpdateCustomer.setUsername(updateRequestDTO.username());
        toUpdateCustomer.setPhoneNumber(updateRequestDTO.phoneNumber());

        repository.save(toUpdateCustomer);
        return true;
    }

    public boolean delete(UUID uuid) throws AccessDeniedException {
        Customer customer = repository.findById(uuid)
                .orElseThrow(() -> new CustomerNotFoundException(uuid));
        if (!canExecutePrivateAction(uuid)) return false;

        repository.delete(customer);
        return true;
    }

    /**
     * This method grants a user only can execute update/delete actions with its own profile
     */
    private boolean canExecutePrivateAction(UUID uuid) throws AccessDeniedException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer toVerifyIfCanExecute = repository.findByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException(uuid));

        if (!toVerifyIfCanExecute.getUuid().equals(uuid)) {
            throw new AccessDeniedException("You do not have permission to perform this action.");
        }

        return true;
    }

}
