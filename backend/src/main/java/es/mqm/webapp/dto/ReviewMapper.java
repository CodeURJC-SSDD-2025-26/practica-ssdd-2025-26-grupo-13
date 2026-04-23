package es.mqm.webapp.dto;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;

import es.mqm.webapp.model.Review;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    ReviewDTO toDTO(Review review);
    List<ReviewDTO> toDTOs(Collection<Review> reviews);
    Review toDomain(ReviewDTO reviewDTO);
}
