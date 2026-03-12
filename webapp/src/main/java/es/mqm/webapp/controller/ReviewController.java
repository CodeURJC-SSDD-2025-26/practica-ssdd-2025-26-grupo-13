package es.mqm.webapp.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.mqm.webapp.model.Product;
import es.mqm.webapp.model.Review;
import es.mqm.webapp.model.User;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import es.mqm.webapp.service.ProductService;
import es.mqm.webapp.service.ReviewService;
import es.mqm.webapp.service.UserService;
@Controller
public class ReviewController {
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    @GetMapping("/modify_review/{id}")
    public String showModifyReviewForm(@PathVariable("id") int id,Model model) {
        Review review = reviewService.findById(id).orElse(null);  
        model.addAttribute("name", review.getUser().getName());  
        model.addAttribute("product_name", review.getProduct().getName());
        model.addAttribute("description", review.getDescription());
        return "modify_review"; 
    }

    @GetMapping("/create_review/{product_id}")
    public String showCreateReviewForm(@PathVariable("product_id") int productId, Model model) {
        model.addAttribute("product_id", productId);
        model.addAttribute("cssfile", "styles");
        return  "redirect:/create_review";
    }

    @RequestMapping("/newReview/{product_id}")
    public String newReview(@PathVariable("product_id") int productId, @RequestParam String name, @RequestParam String reviewDescription) { 
        Product product = productService.findById(productId).orElse(null);
        User user = userService.findById(1).orElse(null);
        Review review = new Review(product, user, "No me ha gustado el treto que he recibido", "2024-06-01", 4.0f);
        reviewService.save(review);
        return "user_profile/"; 
    }
    
}
