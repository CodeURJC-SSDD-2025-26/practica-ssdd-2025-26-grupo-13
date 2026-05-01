package es.mqm.dto;

import java.time.LocalDate;

public record MessageInfoDTO(
    Integer id,
    String name,
    String productName,
    String productUserName,
    String buyerId,
    Double productPrice, 
    String surnames,
    String address, 
    String apartment, 
    String city,
    String zipcode,
    String province,
    String country,
    String buyerEmail,
    String sellerEmail,
    String buyerName,
    String phone,
    String creditCardNumberLast4Digits,
    Double totalPrice,
    LocalDate createdAt
) {
}
