package es.mqm.webapp.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OrderDTO(
        Integer id, UserBasicDTO buyer, ProductDTO product,
        String name, String surnames,
        String address, String apartment,
        String zipcode, String city, 
        String province, String country,
        String phone,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) String creditCardNumber,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) String creditCardExpiryDate,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) String creditCardCVV,
        Double totalPrice,
        LocalDate createdAt, LocalDate updatedAt) {
}
