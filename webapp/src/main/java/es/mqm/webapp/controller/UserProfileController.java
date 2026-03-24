package es.mqm.webapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import es.mqm.webapp.repository.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.mqm.webapp.model.Image;
import es.mqm.webapp.model.User;
import es.mqm.webapp.repository.UserRepository;
import es.mqm.webapp.service.UserService;

@Controller
public class UserProfileController {
    @Autowired
    private UserService userService;

    @GetMapping("/user_profile/{id}")
    public String showUserProfile(Model model, @PathVariable String id) {
        int user_id = Integer.parseInt(id);
        User user = userService.findById(user_id).orElse(null);
        model.addAttribute("name", user.getName());
        model.addAttribute("surnames", user.getSurnames());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("password",user.getPassword());
        Image image = user.getImage();
        if (image != null) {
            model.addAttribute("imageUrl", image.getId());
        } else {
            model.addAttribute("imageUrl", "usuario anonimo.jpg");
        }
        model.addAttribute("rating", user.getRating());
        model.addAttribute("location", user.getLocation().getName());
        model.addAttribute("bought", 4); // placeholder
        model.addAttribute("sold", 4);


        
        model.addAttribute("cssfile", "user_profile");
        return "user_profile";
    }
}
