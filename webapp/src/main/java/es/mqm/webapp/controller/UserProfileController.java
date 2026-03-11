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

import es.mqm.webapp.model.User;

@Controller
public class UserProfileController {
    @Autowired
    private UserRepository userRepository;
    public UserProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @GetMapping("/user_profile/{id}")
     public String showUserProfile(Model model, @PathVariable String id) {
        int user_id = Integer.parseInt(id);
        User user = userRepository.findById(user_id).orElse(null);
        model.addAttribute("name", user.getName());
        model.addAttribute("surnames", user.getSurnames());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("password",user.getPassword());
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
    @GetMapping("/modify_user")
    public String showModifyUser(Model model){
        model.addAttribute("cssfile", "sell_product");    
        return "modify_user";
    }

    @PostMapping("/newuser")
    public String createNewUser(Model model, @RequestParam String inputName, @RequestParam String inputSurnames, @RequestParam String inputEmail, @RequestParam String inputPassword ){
        User user = new User( inputName, inputSurnames, inputEmail, inputPassword, "usuario_anonimo.jpg", 5.0, "28012, Madrid", 0, 0);
        userRepository.save(user);
        return "redirect:/";
    }
    @PostMapping("/validlogin")
    public String login(Model model, @RequestParam String inputEmail, @RequestParam String inputPassword){
        return "redirect:/";
    }
    @PostMapping("/login_admin")
    public String loginAdmin(Model model, @RequestParam String inputEmail, @RequestParam String inputPassword){
        return "redirect:/administrator_dashboard";
    }
    

    

    
}
