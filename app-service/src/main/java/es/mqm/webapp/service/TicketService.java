package es.mqm.webapp.service;

import es.mqm.webapp.dto.MailInfoDTO;
import es.mqm.webapp.model.Order;
import es.mqm.webapp.model.Product;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class TicketService {
   @Value("${utilityservice.url:}") private String baseUrl;

    RestClient restClient = RestClient.create();
    public byte[] generateTicket(Order order) throws RuntimeException {
      Product product = order.getProduct();
      MailInfoDTO info = new MailInfoDTO(order.getId(), order.getName(), product.getName(),product.getUser().getName(),
            String.valueOf(order.getBuyer().getId()), 
            product.getPrice(), order.getSurnames(), order.getAddress(),order.getApartment(),order.getCity(),order.getZipcode(),
            order.getProvince(),order.getCountry(),order.getBuyer().getEmail(), product.getUser().getEmail(), 
            order.getBuyer().getName(), order.getPhone(), order.getCreditCardNumber().substring(order.getCreditCardNumber().length()- 4),
            order.getTotalPrice(), order.getCreatedAt());
       ResponseEntity<byte[]> response = restClient.post()
         .uri(baseUrl + "tickets/")
         .body(info)
         .retrieve()
         .toEntity(byte[].class);
       return response.getBody();
    }
}
