package es.mqm.webapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import es.mqm.webapp.model.Product;
import es.mqm.webapp.model.Review;
import es.mqm.webapp.model.User;
import es.mqm.webapp.repository.ProductRepository;
import es.mqm.webapp.repository.ReviewRepository;
import es.mqm.webapp.repository.UserRepository;

@Controller
public class DatabaseInitializer implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public void run(String... args) throws Exception {
        List<String> categories = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            productRepository.save(new Product("Producto " + (i + 1), "Descripcion", 50, "Vendedor" + (i + 1),
                    "placeholder100x100.png", categories));
        }
        for (int i = 0; i < 5; i++) {
            userRepository.save(new User("Usuario " + (i + 1), "Apellido " + (i + 1), "usuario" + (i + 1) + "@example.com", "1234", "usuario_anonimo.jpg", (float) 4.5, "28012, Madrid", 1, 4));
        }
        for(int i=0; i<3; i++){
            Product product = productRepository.findById(i + 1).orElse(null);
            User user = userRepository.findById(1).orElse(null);
            reviewRepository.save(new Review(product, user, "Comentario " + (i + 1), "2023-01-01", 4.0f));
        }  
    }

}
