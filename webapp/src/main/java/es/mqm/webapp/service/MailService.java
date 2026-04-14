package es.mqm.webapp.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import es.mqm.webapp.model.Order;
import es.mqm.webapp.model.Product;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPlainText(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    @Async // prevents webapp waiting until email is sent
    public void sendOrderConfirmation(Order order) {
        ClassPathResource template = new ClassPathResource("templates/orderemail.html");
        String html;
        Product product = order.getProduct();
        try (var inputStream = template.getInputStream()) {
            html = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8)
                .replace("{{date}}", order.getCreatedAt().format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", Locale.of("es", "ES"))))
                .replace("{{name}}",order.getName())
                .replace("{{productname}}",product.getName())
                .replace("{{productquality}}", product.getState())
                .replace("{{productprice}}", String.valueOf(product.getPrice()))
                .replace("{{surnames}}", order.getSurnames())
                .replace("{{address}}", order.getAddress())
                .replace("{{apartment}}", order.getApartment())
                .replace("{{zipcode}}", order.getZipcode())
                .replace("{{city}}", order.getCity())
                .replace("{{province}}", order.getProvince())
                .replace("{{country}}", order.getCountry())
                .replace("{{id}}", String.valueOf(order.getId()));
        } catch (IOException e) {
            System.err.println("Error while getting html template");
            return;
        }
        ClassPathResource vendorTemplate = new ClassPathResource("templates/ordervendoremail.html");
        String vendorHtml;
        try (var inputStream = vendorTemplate.getInputStream()) {
            vendorHtml = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8)
                    .replace("{{date}}", order.getCreatedAt().format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", Locale.of("es", "ES"))))
                    .replace("{{name}}", product.getUser().getName())
                    .replace("{{userid}}", String.valueOf(order.getBuyer().getId()))
                    .replace("{{buyername}}", order.getName())
                    .replace("{{buyersurnames}}", order.getSurnames())
                    .replace("{{productname}}", product.getName())
                    .replace("{{productprice}}", String.valueOf(product.getPrice()))
                    .replace("{{address}}", order.getAddress())
                    .replace("{{apartment}}", order.getApartment())
                    .replace("{{zipcode}}", order.getZipcode())
                    .replace("{{city}}", order.getCity())
                    .replace("{{province}}", order.getProvince())
                    .replace("{{country}}", order.getCountry())
                    .replace("{{shippingdeadline}}", order.getCreatedAt().plusDays(3).format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", Locale.of("es", "ES"))))
                    .replace("{{id}}", String.valueOf(order.getId()));
        } catch (IOException e) {
            System.err.println("Error while getting html template");
            return;
        }
        sendHtml(order.getBuyer().getEmail(), "Pedido confirmado!", html);
        sendHtml(product.getUser().getEmail(), "Has vendido un producto!", vendorHtml);
    }

    private void sendHtml(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); 
            mailSender.send(message);
        } catch (Exception e) {
            throw new IllegalStateException("Error while sending email", e);
        }
    }

   
}