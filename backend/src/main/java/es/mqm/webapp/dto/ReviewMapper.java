package es.mqm.webapp.dto;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.mqm.webapp.model.Review;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    ReviewDTO toDTO(Review review);
    List<ReviewDTO> toDTOs(Collection<Review> reviews);
    @Mapping (target = "id", ignore = true)
    @Mapping (target = "date", ignore = true)
    Review toDomain(ReviewDTO reviewDTO);
}
