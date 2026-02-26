package es.mqm.webapp.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class CreateReviewController {
    @GetMapping("/create_review")
    public String showCreateReviewForm(Model model) {
        return "create_review"; 
    }
}