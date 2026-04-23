package es.mqm.dto;


public record ProductBasicDTO(
        int id,
        String name,
        double price,
        UserBasicDTO user,
        String category
) {
}
