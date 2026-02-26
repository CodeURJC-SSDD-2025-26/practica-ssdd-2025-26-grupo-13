package es.mqm.webapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.mqm.webapp.model.Product;
import es.mqm.webapp.model.Review;
import es.mqm.webapp.model.User;

@Controller
public class AdministratorDashboardController {
    private List<User> users= new ArrayList<User>();
    private List<Product> products = new ArrayList<Product>();
    private List<Review> reviews = new ArrayList<Review>(); 
    private int userCounter = 33550;
    private int productCounter = 120730;
    private int productSoldCounter = 570980;
    private List<Integer> categoriesSold = new ArrayList<Integer>();
    private List<Integer> reviewsRating = new ArrayList<Integer>();
    private List<Integer> newUsersPerMonth = new ArrayList<Integer>();
    @GetMapping("/administrator_dashboard")
    public String showAdministratorDashboardPage(Model model) {
        users.clear();
        products.clear();
        reviews.clear();
        categoriesSold.clear();
        reviewsRating.clear();
        newUsersPerMonth.clear();

        for(int i=0;i<3;i++){
            users.add(new User(i+1, "Nombre", "Apellido", "nombre" + (i+1) + "@example.com", "usuario anonimo.jpg", (float) 4.5, "28012, Madrid", 1, 4));
        }
        for(int i=0;i<3;i++){
            products.add(new Product(i+1, "Producto " + (i + 1), "Categoria", 50, "Vendedor" + (i+1), "placeholder100x100.png", new ArrayList<String>()));
        }
        for(int i=0;i<3;i++){
            reviews.add(new Review(i+1,"iPhone XR", "" + (i+1), "1","Muy buen producto, gran calidad precio","Hace 6 meses", (float) 4.0));
        }
        for(int i=0;i<5;i++){
            categoriesSold.add((int) Math.floor(Math.random() * (250 - 50 + 1)) + 50);
        }
        for(int i=0;i<12;i++){
            newUsersPerMonth.add((int) Math.floor(Math.random() * (1000 - 200 + 1)) + 200);
        }
        for(int i=0;i<5;i++){
            reviewsRating.add((int) Math.floor(Math.random() * (200 - 30 + 1)) + 30);
        }
        model.addAttribute("users", users);
        model.addAttribute("products",products);
        model.addAttribute("reviews",reviews);
        model.addAttribute("userCounter", userCounter);
        model.addAttribute("productCounter", productCounter);
        model.addAttribute("productSoldCounter", productSoldCounter);
        String categoriesSoldJson = categoriesSold.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
        model.addAttribute("categoriesSoldJson", categoriesSoldJson);
        String reviewsRatingJson = reviewsRating.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
        model.addAttribute("reviewsRatingJson", reviewsRatingJson);
        String newUsersPerMonthJson = newUsersPerMonth.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
        model.addAttribute("newUsersPerMonthJson", newUsersPerMonthJson);
        return "administrator_dashboard";
    }
}
