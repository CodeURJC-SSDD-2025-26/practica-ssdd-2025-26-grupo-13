package es.mqm.webapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.mqm.webapp.model.Product;
import es.mqm.webapp.model.Review;
import es.mqm.webapp.model.User;
import es.mqm.webapp.service.ProductService;
import es.mqm.webapp.service.UserService;

@Controller
public class AdministratorDashboardController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    private List<User> users = new ArrayList<User>();
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
            users.add(new User( "Nombre", "Apellido", "nombre" + (i+1) + "@example.com", "1234", "usuario anonimo.jpg", (float) 4.5, "28012, Madrid", 1, 4));
        }
        for (int i = 0; i < 3; i++) {
            User user = userService.findById(1).orElse(null);
            products.add(new Product("Producto " + (i + 1), "Categoria", 50, user, "placeholder100x100.png", "informatica", "Madrid"));
        }
        for(int i=0;i<3;i++){
            Product product=productService.findById(i + 1).orElse(null);
            User user=userService.findById(1).orElse(null);
            reviews.add(new Review(product, user, "Comentario " + (i + 1), "2023-01-01", 4.0f));
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

        model.addAttribute("cssfile", "administrator_dashboard"); 
        return "administrator_dashboard";
    }
}
