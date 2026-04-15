package es.mqm.webapp.controller;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.regex.Pattern;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.mqm.webapp.model.Image;
import es.mqm.webapp.model.Location;
import es.mqm.webapp.model.User;
import es.mqm.webapp.service.ImageService;
import es.mqm.webapp.service.LocationService;
import es.mqm.webapp.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AccountController {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    @Value("${google.maps.api-key:}")
    private String mapsApiKey; // stored in application-dev.properties (not in github)

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

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
            model.addAttribute("toastMessage", "Correo o contraseña no válidos. Revisa tus datos.");
        }
        return "login";
    }

    @GetMapping("/administrator_login")
    public String showAdminLoginForm(Model model) {
        model.addAttribute("cssfile", "register");
        return "administrator_login";
    }

    @PreAuthorize("@userService.isOwnerOrAdmin(#id, authentication)")
    @GetMapping("modify_user/{id}")
    public String showModifyUserForm(@PathVariable int id, Model model) {
        model.addAttribute("cssfile", "product");
        User user = userService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        model.addAttribute("user", user);
        model.addAttribute("id", user.getId());
        Image image = user.getImage();
        if (image != null) {
            model.addAttribute("imageUrl", image.getId());
        } else {
            model.addAttribute("imageUrl", "usuario anonimo.jpg");
        }
        return "modify_user";
    }

    @PreAuthorize("@userService.isOwnerOrAdmin(#id, authentication)")
    @PostMapping("/modify_user")
    public String modifyUser(Model model, @RequestParam int id, @RequestParam String name,
            @RequestParam String surnames, @RequestParam String email, @RequestParam(required=false) String password,
            @RequestParam MultipartFile image, RedirectAttributes redirAttr, @RequestParam() String city, @RequestParam() String latitude, @RequestParam() String longitude) throws IOException {
        int user_id = id;
        User user = userService.findById(user_id).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        if (!user.getEmail().equals(email) && userService.findByEmail(email).isPresent()) {
            redirAttr.addFlashAttribute("error", true);
            redirAttr.addFlashAttribute("toastMessage", "El correo introducido ya está registrado.");
            return "redirect:/modify_user/" + user_id;
        }
        user.setName(name);
        user.setSurnames(surnames);
        user.setEmail(email);
        if (password!=null && !password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
        }
        if(!image.isEmpty()){
            Image im = imageService.createImage(image);
            userService.addImageToUser(user.getId(),im);
        }
        double lat=Double.parseDouble(latitude); 
        double lon=Double.parseDouble(longitude); 
        if (user.getLocation().getLatitude()!=lat || user.getLocation().getLongitude()!=lon) { 
            Location loc = new Location(city, lat, lon); 
            user.setLocation(loc);
        }
        userService.save(user);
        return "redirect:/user_profile/" + user_id;
    }

    @PostMapping("/register")
    public String createNewUser(HttpServletRequest request, Model model, @RequestParam String inputName, @RequestParam String inputSurnames,
            @RequestParam String inputEmail, @RequestParam String inputPassword,
            @RequestParam String city, @RequestParam String latitude, @RequestParam String longitude, RedirectAttributes redirAttr) {
        inputEmail = inputEmail.trim();
        if (!EMAIL_PATTERN.matcher(inputEmail).matches()) {
            redirAttr.addFlashAttribute("error", true);
            redirAttr.addFlashAttribute("toastMessage", "El correo introducido no es valido");
            return "redirect:/register";
        }
        if (userService.findByEmail(inputEmail).isPresent()) {
            redirAttr.addFlashAttribute("error", true);
            redirAttr.addFlashAttribute("toastMessage", "El correo introducido ya está registrado");
            return "redirect:/register";
        }
        if(city.isBlank() || latitude.isBlank() || longitude.isBlank()) {
            redirAttr.addFlashAttribute("error", true);
            redirAttr.addFlashAttribute("toastMessage", "La dirección introducida no es válida");
            return "redirect:/register";
        }
        Image image = new Image();
        try (InputStream inputStream = new ClassPathResource("static/images/usuario anonimo.jpg").getInputStream()) {
            image.setImageFile(new SerialBlob(inputStream.readAllBytes()));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to load default user image");
        }
        Location loc = locationService.findByCity(city).orElse(null);
        if (loc == null) {
            loc = new Location(city, Double.parseDouble(latitude), Double.parseDouble(longitude));
            locationService.save(loc);
        }
        User user = new User(inputName, inputSurnames, inputEmail, image, passwordEncoder.encode(inputPassword), loc, "USER");
        user.setCreatedAt(LocalDate.now());
        userService.save(user);
        try { // auto login after completing registration
            request.login(inputEmail, inputPassword);
        } catch (ServletException e) {
            return "redirect:/login";
        }
        return "redirect:/";
    }
}
