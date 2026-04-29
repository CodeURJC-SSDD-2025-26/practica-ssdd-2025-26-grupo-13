package es.mqm.webapp.dto;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.mqm.webapp.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

	UserDTO toDTO(User user);
    List<UserDTO> toDTOs(Collection<User> users);
	@Mapping(target = "image", ignore = true)
	User toDomain(UserDTO userDTO);
}