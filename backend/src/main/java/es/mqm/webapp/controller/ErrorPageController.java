package es.mqm.webapp.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.stereotype.Controller;

@Controller
public class ErrorPageController implements ErrorController {
    @GetMapping("/error")
    public String showErrorPage(Model model, HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        model.addAttribute("status", status != null ? status : "");
        model.addAttribute("message",
                message != null && !message.toString().isBlank() ? message : "Ha ocurrido un error inesperado.");
        model.addAttribute("cssfile", "error");
        return "error";
    }
}
