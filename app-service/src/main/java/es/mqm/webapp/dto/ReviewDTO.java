package es.mqm.webapp.dto;

public record ReviewDTO (
        Integer id,
        ProductBasicDTO product,
        UserBasicDTO user,
        String description,
        String date,
        float rating
){
}
