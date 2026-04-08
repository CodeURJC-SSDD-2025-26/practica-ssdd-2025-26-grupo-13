package es.mqm.webapp.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import es.mqm.webapp.model.Image;
import es.mqm.webapp.model.Location;
import es.mqm.webapp.model.Order;
import es.mqm.webapp.model.Product;
import es.mqm.webapp.model.Review;
import es.mqm.webapp.model.User;
import es.mqm.webapp.service.LocationService;
import es.mqm.webapp.service.OrderService;
import es.mqm.webapp.service.ProductService;
import es.mqm.webapp.service.ReviewService;
import es.mqm.webapp.service.UserService;
import es.mqm.webapp.service.MailService;

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

    @Autowired
    private OrderService orderService;

    @Override
    public void run(String... args) throws Exception {
        LocalDate today = LocalDate.now();
        Location loc = new Location("Madrid", 40.4168, -3.7038);
        locationService.save(loc);
        for (int i = 0; i < 5; i++) {
            Image image = new Image();
            try (InputStream inputStream = new ClassPathResource("static/images/usuario anonimo.jpg").getInputStream()) {
                image.setImageFile(new SerialBlob(inputStream.readAllBytes()));
            } catch (Exception e) {
                throw new RuntimeException("Failed to load default user image", e);
            }
            User user = new User("Usuario " + (i + 1), "Apellido " + (i + 1), "usuario" + (i + 1) + "@example.com", image, passwordEncoder.encode("1234"),  (float) 4.5, loc, "USER");
            int daysBack = ThreadLocalRandom.current().nextInt(0, 365);
            user.setCreatedAt(today.minusDays(daysBack)); //user created in the past year
            userService.save(user);
        }
        Image image = new Image();
        try (InputStream inputStream = new ClassPathResource("static/images/admin_icon.png").getInputStream()) {
            image.setImageFile(new SerialBlob(inputStream.readAllBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load default admin image", e);
        }
        User admin = new User("Admin", "Admin", "admin@admin.com", image, passwordEncoder.encode("1234"),  (float) 4.5, loc, "USER", "ADMIN");
        int adminDaysBack = ThreadLocalRandom.current().nextInt(0, 365);
        admin.setCreatedAt(today.minusDays(adminDaysBack));
        userService.save(admin);
        
        
        for (int i = 0; i < 40; i++) {
            User user= userService.findById(1).orElse(null);
            Image imProduct = new Image();
            try (InputStream inputStream = new ClassPathResource("static/images/product-400x600.png").getInputStream()) {
                imProduct.setImageFile(new SerialBlob(inputStream.readAllBytes()));
            } catch (Exception e) {
                throw new RuntimeException("Failed to load default product image", e);
            }
            String category = switch (i % 5) {
                case 0 -> "automoviles";
                case 1 -> "informatica";
                case 2 -> "electrodomesticos";
                case 3 -> "ropa";
                default -> "libros";
            };
            productService.save(new Product("Producto " + (i + 1), "Buen estado", "Descripcion", 50 + i, user,
                    imProduct, category));
        }
        for(int i=0; i<3; i++){
            Product product = productService.findById(i + 1).orElse(null);
            User user = userService.findById(1).orElse(null);
            reviewService.save(new Review(product, user, "Comentario " + (i + 1), "2023-01-01", 1.0f));
            user=userService.findById(i+1).orElse(null);
            product= productService.findById(1).orElse(null); 
            if (i==0) {
                orderService.save(new Order(user, product, "Jose", "Perez", "C/ Tulipán", "s/n",
                "28933", "Móstoles", "Madrid", "España", "+34654246502", "4242424242424242", "12/27",
                "234", 50.5));
                product.setIsSold(true);
                productService.save(product);
            }
            reviewService.save(new Review(product, user, "Comentario " + 2, "2023-01-01", 3.0f));
        }
    }
}
