package es.mqm.webapp.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
        model.addAttribute("cssfile", "sell_product");
        Review review = reviewService.findById(id).orElse(null);  
        model.addAttribute("name", (review.getUser()).getName());  
        model.addAttribute("product_name", (review.getProduct()).getName());
        model.addAttribute("description", review.getDescription());
        return "modify_review"; 
    }

    @RequestMapping("/modifyReview")
    public String modifyReview(@RequestParam int id, @RequestParam String description, @RequestParam float rating) {
        Review review = reviewService.findById(id).orElse(null);
        if (review != null) {
            review.setDescription(description);
            review.setRating(rating);
            reviewService.save(review);
        }
        return "redirect:/user_profile/" + review.getUser().getId();
    }

    @GetMapping("/create_review/{product_id}")
    public String showCreateReviewForm(@PathVariable("product_id") int productId, Model model) {
        Product product = productService.findById(productId).orElse(null);
        model.addAttribute("product", product);
        model.addAttribute("cssfile", "styles");
        return  "create_review";
    }

    @PostMapping("/newReview")
    public String newReview(Review review) { 
        reviewService.save(review);
        return "redirect:/user_profile/1"; 
    }
    
}
