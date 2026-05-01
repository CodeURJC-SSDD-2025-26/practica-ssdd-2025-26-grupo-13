package es.mqm.webapp.dto;

import java.time.LocalDate;

public record MailInfoDTO(
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
