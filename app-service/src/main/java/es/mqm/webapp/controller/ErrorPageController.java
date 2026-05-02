package es.mqm.webapp.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.stereotype.Controller;

@Controller
public class ErrorPageController implements ErrorController {
    @GetMapping("/error")
    public Object showErrorPage(Model model, HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object pathAttr = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        String path = pathAttr != null ? pathAttr.toString() : request.getRequestURI();
        int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        if (status != null) {
            try {
                statusCode = Integer.parseInt(status.toString());
            } catch (NumberFormatException ex) {
                statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            }
        }

        if (path != null && path.startsWith("/api/")) {
            Map<String, Object> body = new LinkedHashMap<>();
            HttpStatus resolvedStatus = HttpStatus.resolve(statusCode);
            body.put("error", resolvedStatus != null ? resolvedStatus.getReasonPhrase() : "Error");
            body.put("message", message != null && !message.toString().isBlank()
                ? message
                : "Unexpected error");
            body.put("path", path);
            return ResponseEntity.status(statusCode).body(body);
        }

        model.addAttribute("status", status != null ? status : "");
        model.addAttribute("message",
                message != null && !message.toString().isBlank() ? message : "Ha ocurrido un error inesperado.");
        model.addAttribute("cssfile", "error");
        return "error";
    }
}
