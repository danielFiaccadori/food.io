package net.dndats.foodio.adapters.mapper;

import net.dndats.foodio.application.dto.restaurant.RestaurantDetailsDTO;
import net.dndats.foodio.application.dto.restaurant.SignUpRestaurantRequest;
import net.dndats.foodio.domain.model.Restaurant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    RestaurantDetailsDTO toDetailsDTO(Restaurant restaurant);
    Restaurant toRestaurantSignUpRequest(SignUpRestaurantRequest request);

}
