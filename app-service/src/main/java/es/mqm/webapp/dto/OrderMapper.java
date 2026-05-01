package es.mqm.webapp.dto;
import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.mqm.webapp.model.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
	OrderDTO toDTO(Order order);
    List<OrderDTO> toDTOs(Collection<Order> orders);
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	Order toDomain(OrderDTO order);
}