package es.mqm.webapp.dto;

public record ReviewDTO (
        int id,
        ProductBasicDTO productId,
        UserBasicDTO userId,
        String description,
        String date,
        float rating
){
}
