package es.mqm.webapp.dto;

import java.util.List;

import org.mapstruct.Mapper;

import es.mqm.webapp.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

	UserDTO toDTO(User user);
    List<UserDTO> toDTOs(List<User> users);
	User toDomain(UserDTO userDTO);
}