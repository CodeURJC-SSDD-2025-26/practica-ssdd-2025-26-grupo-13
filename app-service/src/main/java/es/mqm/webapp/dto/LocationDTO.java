package es.mqm.webapp.dto;

public record LocationDTO(
    Integer id,
    String name, 
    Double latitude, 
    Double longitude
) {
}
