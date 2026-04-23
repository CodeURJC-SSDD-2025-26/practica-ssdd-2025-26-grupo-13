package es.mqm.webapp.dto;

public record ReviewDTO (
        int id,
        ProductDTO productId,
        UserDTO userId,
        String description,
        String date,
        float rating
){
}
