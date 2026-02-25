package es.mqm.webapp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdministratorDashboardController {
    private List<User> users= new ArrayList<User>();
    private List<Product> products = new ArrayList<Product>();
    private List<Review> reviews = new ArrayList<Review>(); 
    @GetMapping("/administrator_dashboard")
    public String showAdministratorDashboardPage(Model model) {
        for(int i=0;i<3;i++){
            users.add(new User(i+1, "Nombre", "Apellido", "nombre" + (i+1) + "@example.com", "usuario anonimo.jpg", (float) 4.5, "28012, Madrid", 1, 4));
        }
        for(int i=0;i<3;i++){
            products.add(new Product(i+1, "Producto " + (i + 1), "Categoria", 50, "Vendedor" + (i+1), "placeholder100x100.png", new ArrayList<String>()));
        }
        for(int i=0;i<3;i++){
            reviews.add(new Review(i+1,"iPhone XR", "Usuario " + (i+1), "Usuario 1", "Muy buen producto, gran calidad precio","Hace 6 meses", (float) 4.0));
        }
        model.addAttribute("users", users);
        model.addAttribute("products",products);
        model.addAttribute("reviews",reviews);
        return "administrator_dashboard";
    }
}