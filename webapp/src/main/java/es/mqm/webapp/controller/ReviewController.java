package es.mqm.webapp.controller;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import es.mqm.webapp.model.Product;
import es.mqm.webapp.model.Review;
import es.mqm.webapp.model.User;
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
        if(review==null){
            return "redirect:/error";
        }  
        model.addAttribute("initialRating", Math.max(0, Math.min(5, Math.round(review.getRating()))));
        model.addAttribute("review", review);  
        return "modify_review"; 
    }

    @PostMapping("/change_review")
    public String modifyReview(@RequestParam int id, @RequestParam String description, @RequestParam float rating) {
        Review review = reviewService.findById(id).orElse(null);
        if (review != null) {
            review.setDescription(description);
            review.setRating(normalizeRating(rating));
            reviewService.save(review);
        }
        return "redirect:/user_profile/" + review.getUser().getId();
    }

    @GetMapping("/delete_review/{id}")
    public String deleteReview(@PathVariable("id") int id) {
        Review review = reviewService.findById(id).orElse(null);
        if (review != null) {
            int userId = review.getUser().getId();
            reviewService.deleteById(id);
            return "redirect:/user_profile/" + userId;
        }
        return "redirect:/error";
    }
    
    @GetMapping("/create_review/{product_id}")
    public String showCreateReviewForm(@PathVariable("product_id") int productId, Model model) {
        Product product = productService.findById(productId).orElse(null);
        model.addAttribute("product", product);
        model.addAttribute("initialRating", 0);
        model.addAttribute("cssfile", "styles");
        return  "create_review";
    }

    @PostMapping("/new_review")
    public String newReview(@RequestParam int product, @RequestParam int user, @RequestParam String description, @RequestParam float rating) {
        Product reviewProduct = productService.findById(product).orElse(null);
        User reviewUser = userService.findById(user).orElse(null);

        if (reviewProduct == null || reviewUser == null) {
            return "redirect:/error";
        }

        Review review = new Review();
        review.setProduct(reviewProduct);
        review.setUser(reviewUser);
        review.setDescription(description);
        review.setDate(LocalDate.now().toString());
        review.setRating(normalizeRating(rating));
        reviewService.save(review);
        return "redirect:/user_profile/" + review.getUser().getId(); 
    }

    private float normalizeRating(float rating) {
        int roundedRating = Math.round(rating);
        return Math.max(1, Math.min(5, roundedRating));
    }
    
}
