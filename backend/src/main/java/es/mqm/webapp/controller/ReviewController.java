package es.mqm.webapp.controller;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import es.mqm.webapp.model.Product;
import es.mqm.webapp.model.Review;
import es.mqm.webapp.model.User;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.mqm.webapp.service.ProductService;
import es.mqm.webapp.service.ReviewService;
@Controller
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ProductService productService;

    @PreAuthorize("@reviewService.isUserOrAdmin(#id, authentication)")
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

    @PreAuthorize("@reviewService.isUserOrAdmin(#id, authentication)")
    @PostMapping("/change_review")
    public String modifyReview(@RequestParam int id, @RequestParam String description, @RequestParam float rating, RedirectAttributes redirAttr) {
        Review review = reviewService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reseña no encontrada"));
        review.setDescription(description);
        review.setRating(normalizeRating(rating));
        reviewService.save(review);
        redirAttr.addFlashAttribute("toastMessage", "Reseña modificada correctamente");
        return "redirect:/user_profile/" + review.getProduct().getUser().getId();
    }

    @PreAuthorize("@reviewService.isUserOrAdmin(#id, authentication)")
    @PostMapping("/delete_review/{id}")
    public String deleteReview(@PathVariable("id") int id, RedirectAttributes redirAttr) {
        Review review = reviewService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reseña no encontrada"));            
        reviewService.deleteById(id);
        redirAttr.addFlashAttribute("toastMessage", "Reseña eliminada correctamente");
        return "redirect:/user_profile/" + review.getProduct().getUser().getId();
    }
    
    @PreAuthorize("@orderService.isBuyerOrAdminReview(#productId, authentication)")
    @GetMapping("/create_review/{product_id}")
    public String showCreateReviewForm(@PathVariable("product_id") int productId, Model model) {
        Optional<Review> existingReview = reviewService.findByProductId(productId);
        if (existingReview.isPresent()) {
            return "redirect:/modify_review/" + existingReview.get().getId(); 
        }
        Product product = productService.findById(productId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        model.addAttribute("product", product);
        model.addAttribute("initialRating", 0);
        model.addAttribute("cssfile", "sell_product");
        return  "create_review";
    }

    @PreAuthorize("@orderService.isBuyerOrAdminReview(#product, authentication)")
    @PostMapping("/new_review")
    public String newReview(@RequestParam int product, @RequestParam String description, @RequestParam float rating,RedirectAttributes redirAttr, Model model) {
        Product reviewProduct = productService.findById(product).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        User reviewUser = (User) model.getAttribute("currentUser");
        if (reviewUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }

        Review review = new Review();
        review.setProduct(reviewProduct);
        review.setUser(reviewUser);
        review.setDescription(description);
        review.setRating(normalizeRating(rating));
        reviewService.save(review);
        redirAttr.addFlashAttribute("toastMessage", "Reseña creada correctamente");
        return "redirect:/user_profile/" + review.getProduct().getUser().getId(); 
    }

    private float normalizeRating(float rating) {
        int roundedRating = Math.round(rating);
        return Math.max(1, Math.min(5, roundedRating));
    }
    
}
