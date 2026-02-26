package es.mqm.webapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.mqm.webapp.model.User;

@Controller
public class UserProfileController {
    
    
    
    @GetMapping("/user_profile/{user_id}")
     public String showUserProfile(Model model, @PathVariable String user_id) {
        User user = new User(1, "Usuario " + user_id, "Apellido", "usuario" + user_id + "@example.com", "usuario anonimo.jpg", (float) 4.5, "28012, Madrid", 1, 4);
        model.addAttribute("name", user.getName());
        model.addAttribute("surnames", user.getSurnames());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("imageUrl", user.getImageUrl());
        model.addAttribute("rating", user.getRating());
        model.addAttribute("location", user.getLocation());
        model.addAttribute("bought", user.getBought());
        model.addAttribute("sold", user.getSold());


        
        model.addAttribute("cssfile", "user_profile");
        model.addAttribute("products", user.getProducts());
        model.addAttribute("reviews", user.getReviews());
        return "user_profile";
    }

    
}
