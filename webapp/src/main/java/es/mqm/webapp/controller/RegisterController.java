package es.mqm.webapp.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class RegisterController {
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("cssfile", "register");
        return "register";
    }
}
