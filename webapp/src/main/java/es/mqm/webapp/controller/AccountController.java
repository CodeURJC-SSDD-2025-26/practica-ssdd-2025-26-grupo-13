package es.mqm.webapp.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class AccountController {
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("cssfile", "register");
        return "register";
    }
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("cssfile", "register");
        return "login";
    }
    @GetMapping("/administrator_login")
    public String showAdminLoginForm(Model model) {
        model.addAttribute("cssfile", "register"); 
        return "administrator_login"; 
    }
}
