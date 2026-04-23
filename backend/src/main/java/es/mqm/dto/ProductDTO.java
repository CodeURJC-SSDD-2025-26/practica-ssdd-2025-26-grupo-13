package es.mqm.dto;

import java.time.LocalDateTime;

public record ProductDTO(
        int id,
        String name,
        String description,
        double price,
        UserBasicDTO user,
        ImageDTO image,
        Boolean isSold,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String category
) {
    
}
