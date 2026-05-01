package es.mqm.webapp.dto;

import java.time.LocalDate;

public record OrderDTO(
        Integer id, UserBasicDTO buyer, ProductDTO product,
        String name, String surnames,
        String address, String apartment,
        String zipcode, String city, 
        String province, String country,
        String phone,  double totalPrice,
        LocalDate createdAt, LocalDate updatedAt) {
}
