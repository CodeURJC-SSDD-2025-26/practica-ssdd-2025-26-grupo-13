package es.mqm.webapp.dto;
import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;

import es.mqm.webapp.model.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {
	LocationDTO toDTO(Location location);
	Location toDomain(LocationDTO location);
	List<LocationDTO> toDTOs(Collection<Location> locations);
}