package net.dndats.food_io.adapters.mapper;

import net.dndats.food_io.application.dto.customer.CustomerDetailsDTO;
import net.dndats.food_io.application.dto.customer.SignUpCustomerRequestDTO;
import net.dndats.food_io.domain.model.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDetailsDTO toCustomerDetailsDTO(Customer customer);
    List<CustomerDetailsDTO> toCustomerDetailsDTOList(List<Customer> customers);
    Customer toCustomer(SignUpCustomerRequestDTO signUpRequestMessage);

}
