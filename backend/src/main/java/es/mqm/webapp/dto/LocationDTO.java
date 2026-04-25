package es.mqm.webapp.dto;

public record LocationDTO(
    int id,
    String name, 
    double latitude, 
    double longitude
) {
}
