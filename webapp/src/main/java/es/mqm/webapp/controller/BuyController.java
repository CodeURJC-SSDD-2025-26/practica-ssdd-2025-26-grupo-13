package es.mqm.webapp.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.mqm.webapp.model.Product;
import es.mqm.webapp.service.ProductService;

@Controller
public class BuyController {

    @Autowired
    private ProductService productService;

    @GetMapping("/buy/{id}")
    public String buy(Model model, @PathVariable int id) {

        Optional<Product> productOpt = productService.findById(id);
        if (!productOpt.isPresent() || productOpt.get().getIsSold()) {
            return "redirect:/";
        }
        Product product = productOpt.get();
        System.out.println(product);
        model.addAttribute("product", product);
        model.addAttribute("priceWithShipping", product.getPrice() + 3.5);
        model.addAttribute("cssfile", "buy");
        model.addAttribute("loggedin", true);
        return "buy";
    }
}