package es.mqm.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserProfileController {
     @GetMapping("/user_profile")
     public String showUserProfile(Model model) {
        model.addAttribute("cssfile", "user_profile");
        return "user_profile";
    }
}
