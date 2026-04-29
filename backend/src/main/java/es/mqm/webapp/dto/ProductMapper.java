package es.mqm.webapp.dto;
import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.mqm.webapp.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDTO(Product product);
    ProductBasicDTO toBasicDTO(Product product);
    List<ProductDTO> toDTOs(Collection<Product> products);
    @Mapping(target = "image", ignore = true)
    Product toDomain(ProductDTO productDTO);

}
