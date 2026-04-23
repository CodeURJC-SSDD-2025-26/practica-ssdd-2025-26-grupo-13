package es.mqm.webapp.dto;


public record ProductBasicDTO(
        int id,
        String name,
        double price,
        UserBasicDTO user,
        String category
) {
}
