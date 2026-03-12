package es.mqm.webapp.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.mqm.webapp.model.Review;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReviewController {

    @GetMapping("/modify_review/{id}")
    public String showModifyReviewForm(@PathVariable("id") int id,Model model) {
        Review review = new Review("barbacoa", "Lucas Martinez", "Usuario Destino", "No me ha gustado el treto que he recibido", "2024-06-01", 4.0f);  
        Review review = new Review("barbacoa", "Lucas Martinez", "Usuario Destino", "No me ha gustado el treto que he recibido", "2024-06-01", 4.0f);  
        model.addAttribute("cssfile", "sell_product");
        model.addAttribute("name", review.getUserId());  
        model.addAttribute("product_name", review.getProduct());
        model.addAttribute("description", review.getDescription());
        return "modify_review"; 
    }

    @GetMapping("/create_review/{product_id}")
    public String showCreateReviewForm(@PathVariable("product_id") int productId, Model model) {
        model.addAttribute("product_id", productId);
        model.addAttribute("cssfile", "styles");
        return "create_review";
    }

    @RequestMapping("/newReview/{product_id}")
    public String newReview(@PathVariable("product_id") int productId, @RequestParam String name, @RequestParam String reviewDescription) { 
        Review review = new Review( "product_id", name, "name", reviewDescription, "2024-06-01", 4.0f);
        return "user_profile/"; 
    }
    
}
