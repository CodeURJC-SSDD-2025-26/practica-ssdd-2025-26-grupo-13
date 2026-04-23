package es.mqm.webapp.dto;
import org.mapstruct.Mapper;

import es.mqm.webapp.model.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {
	LocationDTO toDTO(Location location);
	Location toDomain(LocationDTO location);
}