package es.mqm.webapp.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ExtendedProductDTO(
        int id,
        String name,
        String description,
        double price,
        UserBasicDTO user,
        ImageBasicDTO image,
        Boolean isSold,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String category,
        Integer distance,
        Boolean interestingCategory,
        Double score
) {
}
