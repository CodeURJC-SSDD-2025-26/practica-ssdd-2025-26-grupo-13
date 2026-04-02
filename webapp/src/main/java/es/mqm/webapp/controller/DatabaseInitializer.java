package es.mqm.webapp.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import es.mqm.webapp.model.Image;
import es.mqm.webapp.model.Location;
import es.mqm.webapp.model.Product;
import es.mqm.webapp.model.Review;
import es.mqm.webapp.model.User;
import es.mqm.webapp.service.LocationService;
import es.mqm.webapp.service.ProductService;
import es.mqm.webapp.service.ReviewService;
import es.mqm.webapp.service.UserService;

@Controller
public class DatabaseInitializer implements CommandLineRunner {
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private LocationService locationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Location loc = new Location("Madrid", 40.4168, -3.7038);
        locationService.save(loc);
        for (int i = 0; i < 5; i++) {
            Image image = new Image();
            try (InputStream inputStream = new ClassPathResource("static/images/usuario anonimo.jpg").getInputStream()) {
                image.setImageFile(new SerialBlob(inputStream.readAllBytes()));
            } catch (Exception e) {
                throw new RuntimeException("Failed to load default user image", e);
            }
            userService.save(new User("Usuario " + (i + 1), "Apellido " + (i + 1), "usuario" + (i + 1) + "@example.com", image, passwordEncoder.encode("1234"),  (float) 4.5, loc, "USER"));
        }
        Image image = new Image();
        try (InputStream inputStream = new ClassPathResource("static/images/admin_icon.png").getInputStream()) {
            image.setImageFile(new SerialBlob(inputStream.readAllBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load default admin image", e);
        }
        userService.save(new User("Admin", "Admin", "admin@admin.com", image, passwordEncoder.encode("1234"),  (float) 4.5, loc, "USER", "ADMIN"));
        
        
        for (int i = 0; i < 40; i++) {
            User user= userService.findById(1).orElse(null);
            Image imProduct = new Image();
            try (InputStream inputStream = new ClassPathResource("static/images/product-400x600.png").getInputStream()) {
                imProduct.setImageFile(new SerialBlob(inputStream.readAllBytes()));
            } catch (Exception e) {
                throw new RuntimeException("Failed to load default product image", e);
            }
            productService.save(new Product("Producto " + (i + 1), "Buen estado", "Descripcion", 50 + i, user,
                    imProduct, "automoviles"));
        }
        for(int i=0; i<3; i++){
            Product product = productService.findById(i + 1).orElse(null);
            User user = userService.findById(1).orElse(null);
            reviewService.save(new Review(product, user, "Comentario " + (i + 1), "2023-01-01", 1.0f));
            user=userService.findById(i+1).orElse(null);
            product= productService.findById(1).orElse(null); 
            reviewService.save(new Review(product, user, "Comentario " + 2, "2023-01-01", 3.0f));
        }
    }
}
