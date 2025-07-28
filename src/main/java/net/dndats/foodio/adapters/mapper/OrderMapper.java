package net.dndats.foodio.adapters.mapper;

import net.dndats.foodio.application.dto.order.OrderDetailsDTO;
import net.dndats.foodio.domain.model.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDetailsDTO toDetailsDTO(Order order);

}
