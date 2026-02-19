package es.mqm.webapp;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class AdministratorLoginController {
    @GetMapping("/administrator_login")
    public String showLoginForm(Model model) {
        model.addAttribute("cssfile", "register");
        return "administrator_login";
    }
}
