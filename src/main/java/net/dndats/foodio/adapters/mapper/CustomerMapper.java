package net.dndats.foodio.adapters.mapper;

import net.dndats.foodio.application.dto.customer.CustomerDetailsDTO;
import net.dndats.foodio.application.dto.security.SignUpRequest;
import net.dndats.foodio.domain.model.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDetailsDTO toCustomerDetailsDTO(Customer customer);
    Customer toCustomer(SignUpRequest signUpRequestMessage);

}
