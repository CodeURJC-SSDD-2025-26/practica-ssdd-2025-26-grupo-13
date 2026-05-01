package es.mqm.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import es.mqm.dto.MessageInfoDTO;
import es.mqm.service.MailService;
import es.mqm.service.TicketService;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketRestController {

    @Autowired
    TicketService ticketService; 
    
    @PostMapping("/")
    public ResponseEntity<byte[]> downloadTicket(@RequestBody MessageInfoDTO info) {
        byte[] ticketBytes = ticketService.generateTicket(info);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pedido_" + info.id() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(ticketBytes);
    }
}
