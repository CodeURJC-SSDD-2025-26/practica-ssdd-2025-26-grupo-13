package es.mqm.dto;

public record ReviewDTO (
        int id,
        ProductDTO productId,
        UserDTO userId,
        String description,
        String date,
        float rating
){
}
