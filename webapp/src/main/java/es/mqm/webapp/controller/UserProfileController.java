package es.mqm.webapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        User user = new User("Usuario " + user_id, "Apellido", "usuario" + user_id + "@example.com", "1234", "usuario_anonimo.jpg", (float) 4.5, "28012, Madrid", 1, 4);
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
    @GetMapping("/modify_user/{id}")
    public String showModifyUser(Model model,@PathVariable String id) {
        int user_id = Integer.parseInt(id);
        User user = userService.findById(user_id).orElse(null);
        model.addAttribute("name", user.getName());
        model.addAttribute("surnames", user.getSurnames());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("password",user.getPassword());
        model.addAttribute("cssfile", "sell_product");    
        return "modify_user";
    }

    @RequestMapping("/modify_info/{id}")
    public String modifyUser(Model model, @PathVariable String id, @RequestParam String name, @RequestParam String surn,ames, @RequestParam String email, @RequestParam String password){
        int user_id = Integer.parseInt(id);
        User user = userRepository.findById(user_id).orElse(null);
        user.setName(name);
        user.setSurnames(surnames);
        user.setEmail(email);
        user.setPassword(password);
        userRepository.save(user);
        return "user_profile/{id}";
    }

    @RequestMapping("/newuser")
    public String createNewUser(Model model, @RequestParam String email, 
            @RequestParam String password /*@RequestParam String imageUrl, @RequestParam float rating, 
            @RequestParam String location, @RequestParam int bought, 
            @RequestParam int sold*/){
                User user = new User( "Nuevo Usuario", "Apellido", email, password, "usuario_anonimo.jpg", (float) 4.5, "28012, Madrid", 1, 4);
            /*model.addAttribute("name", name);*/
            model.addAttribute("surnames", surnames);
            model.addAttribute("email", email);
            model.addAttribute("password", password);*/
            /*model.addAttribute("imageUrl", imageUrl);
            model.addAttribute("rating", rating);
            model.addAttribute("location", location);
            model.addAttribute("bought", bought);
            model.addAttribute("sold", sold);*/

            return "index";
    }
    

    

    
}
