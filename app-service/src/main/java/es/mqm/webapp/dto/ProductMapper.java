package es.mqm.webapp.dto;
import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.mqm.webapp.model.Product;
import es.mqm.webapp.model.ExtendedProduct;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDTO(Product product);
    ProductBasicDTO toBasicDTO(Product product);
    List<ProductDTO> toDTOs(Collection<Product> products);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toDomain(ProductDTO productDTO);
    
    @Mapping(source = "p.id", target = "id")
    @Mapping(source = "p.name", target = "name")
    @Mapping(source = "p.description", target = "description")
    @Mapping(source = "p.price", target = "price")
    @Mapping(source = "p.user", target = "user")
    @Mapping(source = "p.image", target = "image")
    @Mapping(source = "p.isSold", target = "isSold")
    @Mapping(source = "p.createdAt", target = "createdAt")
    @Mapping(source = "p.updatedAt", target = "updatedAt")
    @Mapping(source = "p.category", target = "category")
    @Mapping(source = "distance", target = "distance")
    @Mapping(source = "interesting", target = "interestingCategory")
    @Mapping(source = "score", target = "score")
    ExtendedProductDTO toExtendedDTO(ExtendedProduct extendedProduct);

}
