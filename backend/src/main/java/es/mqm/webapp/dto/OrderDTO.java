package es.mqm.webapp.dto;

import java.time.LocalDate;

public record OrderDTO(
        int id, UserBasicDTO buyer, ProductDTO product,
        String name, String surnames,
        String address, String apartment,
        String zipcode, String city, 
        String province, String country,
        String phone, String creditCardNumber,
        String creditCardExpiryDate,
        String creditCardCVV, double totalPrice,
        LocalDate createdAt, LocalDate updatedAt) {
}
