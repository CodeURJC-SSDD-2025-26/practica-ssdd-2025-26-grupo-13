package es.mqm.webapp.dto;

import java.time.LocalDate;

public record OrderBasicDTO(
    int id, ProductDTO product, 
    double totalPrice, 
    LocalDate createdAt, 
    LocalDate updatedAt
) {
}
