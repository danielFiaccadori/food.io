package net.dndats.foodio.adapters.mapper;

import net.dndats.foodio.application.dto.product.ProductDetailsDTO;
import net.dndats.foodio.domain.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDetailsDTO toDetailsDTO(Product product);

}
