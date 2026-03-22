package es.mqm.webapp.controller;
import java.io.InputStream;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.mqm.webapp.model.Image;
import es.mqm.webapp.model.Location;
import es.mqm.webapp.model.User;
import es.mqm.webapp.service.LocationService;
import es.mqm.webapp.service.UserService;


@Controller
public class AccountController {

    @Value("${google.maps.api-key:}")
    private String mapsApiKey; // stored in application-dev.properties (not in github)

    @Autowired
    private UserService userService;

    @Autowired
    private LocationService locationService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("cssfile", "register");
        model.addAttribute("mapsApiKey", mapsApiKey);
        return "register";
    }
    @GetMapping("/login")
    public String showLoginForm(Model model, @RequestParam(required = false, value = "error") String error) {
        model.addAttribute("cssfile", "register");
        if (error != null) {
            model.addAttribute("error", true);
        }
        return "login";
    }
    @GetMapping("/administrator_login")
    public String showAdminLoginForm(Model model) {
        model.addAttribute("cssfile", "register");
        return "administrator_login";
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
    public String modifyUser(Model model, @PathVariable String id, @RequestParam String name,
            @RequestParam String surnames, @RequestParam String email, @RequestParam String password) {
        int user_id = Integer.parseInt(id);
        User user = userService.findById(user_id).orElse(null);
        user.setName(name);
        user.setSurnames(surnames);
        user.setEmail(email);
        user.setPassword(password);
        userService.save(user);
        return "user_profile/{id}";
    }


    @PostMapping("/newuser")
        public String createNewUser(Model model, @RequestParam String inputName, @RequestParam String inputSurnames, @RequestParam String inputEmail, @RequestParam String inputPassword,
            @RequestParam String city, @RequestParam String latitude, @RequestParam String longitude
         ){
        Image image = new Image();
        try (InputStream inputStream = new ClassPathResource("static/images/usuario anonimo.jpg").getInputStream()) {
            image.setImageFile(new SerialBlob(inputStream.readAllBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load default user image", e);
        }
        Location loc = locationService.findByCity(city).orElse(null);
        if (loc == null) {
            loc = new Location(city, Double.parseDouble(latitude), Double.parseDouble(longitude));
            locationService.save(loc);
        }
        User user = new User(inputName, inputSurnames, inputEmail, image, inputPassword, 5.0, loc, "USER");
        userService.save(user);
        return "redirect:/";
    }
    @PostMapping("/validatelogin")
        public String login(Model model, @RequestParam String inputEmail, @RequestParam String inputPassword){
        return "redirect:/";
    }
    @PostMapping("/login_admin")
        public String loginAdmin(Model model, @RequestParam String inputEmail, @RequestParam String inputPassword){
        return "redirect:/administrator_dashboard";
    }

}

