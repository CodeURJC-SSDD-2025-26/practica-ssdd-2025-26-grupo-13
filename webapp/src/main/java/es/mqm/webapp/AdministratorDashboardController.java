package es.mqm.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdministratorDashboardController {
    @GetMapping("/administrator_dashboard")
    public String showAdministratorDashboardPage(Model model) {
        return "administrator_dashboard";
    }
}