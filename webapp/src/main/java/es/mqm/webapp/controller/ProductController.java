package es.mqm.webapp.controller;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import es.mqm.webapp.model.User;
import es.mqm.webapp.service.UserService;

import es.mqm.webapp.model.Product;
@Controller
public class ProductController {
    @Autowired
    private UserService userService;

    @GetMapping("/product")
    public String showProductDetails(Model model) {
        User user = userService.findById(1).orElse(null);
        Product product = new Product("Producto de ejemplo", "Descripción del producto", 19.99f, user, "producto.jpg", "informatica", "Madrid");
        model.addAttribute("cssfile", "product");
        return "product";
    }
    @GetMapping("/sell_product")
    public String showSellProductPage(Model model) {
        model.addAttribute("cssfile", "sell_product");
        return "sell_product";
    }

    @GetMapping("/modify_product")
    public String showModifyProductPage(Model model) {

        User user = userService.findById(1).orElse(null);
        Product product = new Product("Producto", "Descripcion", 50.0, user, "product-400x600.png", "informatica", "Madrid");
        model.addAttribute("cssfile", "sell_product");
        model.addAttribute("product", product);
        model.addAttribute("category", "informatica");
        return "modify_product";
    }
}
