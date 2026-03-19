package es.mqm.webapp.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;

import es.mqm.webapp.model.Image;
import es.mqm.webapp.model.Product;
import es.mqm.webapp.model.Review;
import es.mqm.webapp.model.User;
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

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 5; i++) {
            Image image = new Image();
            try (InputStream inputStream = new ClassPathResource("static/images/usuario anonimo.jpg").getInputStream()) {
                image.setImageFile(new SerialBlob(inputStream.readAllBytes()));
            } catch (Exception e) {
                throw new RuntimeException("Failed to load default user image", e);
            }
            userService.save(new User("Usuario " + (i + 1), "Apellido " + (i + 1), "usuario" + (i + 1) + "@example.com", image, "1234",  (float) 4.5, "28012, Madrid", 1, 4));
        }
        for (int i = 0; i < 40; i++) {
            User user= userService.findById(1).orElse(null);
            productService.save(new Product("Producto " + (i + 1), "buen estado", "Descripcion", 50 + i, user,
                    "placeholder100x100.png", "informatica"));
        }
        
        for(int i=0; i<3; i++){
            Product product = productService.findById(i + 1).orElse(null);
            User user = userService.findById(1).orElse(null);
            reviewService.save(new Review(product, user, "Comentario " + (i + 1), "2023-01-01", 4.0f));
        }  
    }

}
