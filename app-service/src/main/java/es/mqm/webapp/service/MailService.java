package es.mqm.webapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import es.mqm.webapp.dto.MailInfoDTO;
import es.mqm.webapp.model.Order;
import es.mqm.webapp.model.Product;

@Service
public class MailService {
    @Value("${utilityservice.url:}") private String baseUrl;

    RestClient restClient = RestClient.create();

    @Async // prevents webapp waiting until email is sent
    public void sendOrderConfirmation(Order order) {
        Product product = order.getProduct();
        MailInfoDTO info = new MailInfoDTO(order.getId(), order.getName(), product.getName(),product.getUser().getName(),
            String.valueOf(order.getBuyer().getId()), 
            product.getPrice(), order.getSurnames(), order.getAddress(),order.getApartment(),order.getCity(),order.getZipcode(),
            order.getProvince(),order.getCountry(),order.getBuyer().getEmail(), product.getUser().getEmail(), 
            order.getBuyer().getName(), order.getPhone(), order.getCreditCardNumber().substring(order.getCreditCardNumber().length()- 4),
            order.getTotalPrice(), order.getCreatedAt());
        
        ResponseEntity<?> response = restClient.post()
            .uri(baseUrl + "mails/")
            .contentType(MediaType.APPLICATION_JSON)
            .body(info)
            .retrieve()
            .toBodilessEntity();
    }
}
