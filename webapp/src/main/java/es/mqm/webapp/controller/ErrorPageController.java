package es.mqm.webapp.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class ErrorPageController {
    @GetMapping("/error")
    public String showErrorPage(Model model) {
        model.addAttribute("cssfile", "error");
        return "error";
    }
}
