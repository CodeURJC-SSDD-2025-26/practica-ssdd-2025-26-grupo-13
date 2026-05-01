package es.mqm.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.mqm.dto.MessageInfoDTO;
import es.mqm.service.MailService;

@RestController
@RequestMapping("/api/v1/mails")
public class MailRestController {

    @Autowired
    private MailService mailService;

    @PostMapping("/")
    public ResponseEntity<?> sendEmail(@RequestBody MessageInfoDTO info) {
        mailService.sendOrderConfirmation(info);
        return ResponseEntity.ok(
                Map.of(
                    "success", true,
                    "message", "Email request queued"
                )
            );
    } 
}
