package es.mqm.webapp.dto;
import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;

import es.mqm.webapp.model.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
	OrderDTO toDTO(Order order);
    List<OrderDTO> toDTOs(Collection<Order> orders);
	Order toDomain(OrderDTO order);
}