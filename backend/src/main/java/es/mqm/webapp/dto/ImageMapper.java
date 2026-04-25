package es.mqm.webapp.dto;

import org.mapstruct.Mapper;

import es.mqm.webapp.model.Image;

@Mapper(componentModel = "spring")
public interface ImageMapper {

	ImageDTO toDTO(Image image);
	Image toDomain(ImageDTO imageDTO);
	ImageBasicDTO toBasicDTO(Image image);
}
