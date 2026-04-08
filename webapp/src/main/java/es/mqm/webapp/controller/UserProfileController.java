package es.mqm.webapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import es.mqm.webapp.repository.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.mqm.webapp.model.Image;
import es.mqm.webapp.model.Order;
import es.mqm.webapp.model.User;
import es.mqm.webapp.model.Product;
import es.mqm.webapp.model.Review;
import es.mqm.webapp.service.ReviewService;
import es.mqm.webapp.service.ProductService;
import es.mqm.webapp.repository.UserRepository;
import es.mqm.webapp.service.OrderService;
import es.mqm.webapp.service.UserService;

@Controller
public class UserProfileController {
    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/user_profile/{id}")
    public String showUserProfile(Model model, @PathVariable int id) {
        User user = userService.findById(id).orElse(null);
        if (user == null) {
            return "redirect:/error";
        }
        model.addAttribute("id", user.getId());
        model.addAttribute("name", user.getName());
        model.addAttribute("surnames", user.getSurnames());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("password",user.getPassword());
        User currentUser = (User) model.getAttribute("currentUser");
        boolean isUser = currentUser != null && currentUser.getId() == id;
        model.addAttribute("isUser", isUser);
        Image image = user.getImage();
        if (image != null) {
            model.addAttribute("imageUrl", image.getId());
        } else {
            model.addAttribute("imageUrl", "usuario anonimo.jpg");
        }
        model.addAttribute("rating", user.getRating());
        model.addAttribute("location", user.getLocation().getName());
        int bought = orderService.countByBuyer(user);
        int sold = orderService.countBySeller(user);
        model.addAttribute("bought", bought);
        model.addAttribute("sold", sold);

        List<Product> products = productService.findByIsSoldFalseAndUser(user);
        List<Order> orders = orderService.findByBuyer(user);
        List<Review> reviews = reviewService.findByUserDest(id);
        List<Map<String, Object>> reviewsVm = new ArrayList<>();
        for (Review review : reviews) {
            Map<String, Object> item = new HashMap<>();
            item.put("review", review);
            boolean isUserReview = currentUser != null
                    && review.getUser() != null
                    && review.getUser().getId() == currentUser.getId();
            item.put("isUserReview", isUserReview);
            addReviewStars(item, review);
            reviewsVm.add(item);
        }
        model.addAttribute("reviewsVm", reviewsVm);
        model.addAttribute("orders", orders);
        model.addAttribute("products", products);

        if(products.isEmpty()){
            model.addAttribute("emptyProducts", true);
        }else{
            model.addAttribute("emptyProducts", false);
        }

        if(orders.isEmpty()){
            model.addAttribute("emptyOrders", true);
        }else{
            model.addAttribute("emptyOrders", false);
        }

        if(reviews.isEmpty()){
            model.addAttribute("emptyReviews", true);
        }else{
            model.addAttribute("emptyReviews", false);
        }

        model.addAttribute("cssfile", "user_profile");
        return "user_profile";
    }

    private void addReviewStars(Map<String, Object> item, Review review) {
        int rating = Math.max(0, Math.min(5, Math.round(review.getRating())));
        item.put("star1", rating >= 1);
        item.put("star2", rating >= 2);
        item.put("star3", rating >= 3);
        item.put("star4", rating >= 4);
        item.put("star5", rating >= 5);
    }
}
