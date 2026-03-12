package es.mqm.webapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import es.mqm.webapp.model.Product;
import es.mqm.webapp.model.User;
import es.mqm.webapp.repository.ProductRepository;
import es.mqm.webapp.repository.UserRepository;

@Controller
public class DatabaseInitializer implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        List<String> categories = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            productRepository.save(new Product("Producto " + (i + 1), "Descripcion", 50, "Vendedor" + (i + 1),
                    "placeholder100x100.png", categories));
        }
        userRepository.save(new User("Usuario 1", "Apellido", "usuario1@example.com", "1234", "usuario_anonimo.jpg", (float) 4.5, "28012, Madrid", 1, 4));  
    }

}
