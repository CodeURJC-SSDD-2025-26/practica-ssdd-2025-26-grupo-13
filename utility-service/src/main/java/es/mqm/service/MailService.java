package es.mqm.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import es.mqm.dto.MessageInfoDTO;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {

    private final JavaMailSender mailSender;
    @Value("${spring.mail.username:}") private String mailUsername;
    @Value("${spring.mail.password:}") private String mailPassword;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async // prevents webapp waiting until email is sent
    public void sendOrderConfirmation(MessageInfoDTO minfo) {
        if (mailUsername.isBlank() || mailPassword.isBlank()) return;
        ClassPathResource template = new ClassPathResource("templates/orderemail.html");
        String html;
        try (var inputStream = template.getInputStream()) {
            html = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8)
                .replace("{{date}}", minfo.createdAt().format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", Locale.of("es", "ES"))))
                .replace("{{name}}",minfo.name())
                .replace("{{productname}}",minfo.productName())
                .replace("{{productprice}}", String.valueOf(minfo.productPrice()))
                .replace("{{surnames}}", minfo.surnames())
                .replace("{{address}}", minfo.address())
                .replace("{{apartment}}", minfo.apartment())
                .replace("{{zipcode}}", minfo.zipcode())
                .replace("{{city}}", minfo.city())
                .replace("{{province}}", minfo.province())
                .replace("{{country}}", minfo.country())
                .replace("{{id}}", String.valueOf(minfo.id()));
        } catch (IOException e) {
            System.err.println("Error while getting html template");
            return;
        }
        ClassPathResource vendorTemplate = new ClassPathResource("templates/ordervendoremail.html");
        String vendorHtml;
        try (var inputStream = vendorTemplate.getInputStream()) {
            vendorHtml = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8)
                    .replace("{{date}}", minfo.createdAt().format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", Locale.of("es", "ES"))))
                    .replace("{{name}}", minfo.productUserName())
                    .replace("{{userid}}", minfo.buyerId())
                    .replace("{{buyername}}", minfo.name())
                    .replace("{{buyersurnames}}", minfo.surnames())
                    .replace("{{productname}}", minfo.productName())
                    .replace("{{productprice}}", String.valueOf(minfo.productPrice()))
                    .replace("{{address}}", minfo.address())
                    .replace("{{apartment}}", minfo.apartment())
                    .replace("{{zipcode}}", minfo.zipcode())
                    .replace("{{city}}", minfo.city())
                    .replace("{{province}}", minfo.province())
                    .replace("{{country}}", minfo.country())
                    .replace("{{shippingdeadline}}", minfo.createdAt().plusDays(3).format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", Locale.of("es", "ES"))))
                    .replace("{{id}}", String.valueOf(minfo.id()));
        } catch (IOException e) {
            System.err.println("Error while getting html template");
            return;
        }
        sendHtml(minfo.buyerEmail(), "Pedido confirmado!", html);
        sendHtml(minfo.sellerEmail(), "Has vendido un producto!", vendorHtml);
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
