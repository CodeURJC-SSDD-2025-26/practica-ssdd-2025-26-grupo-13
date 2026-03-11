package es.mqm.webapp.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.mqm.webapp.model.Product;
@Controller
public class ProductController {

    @GetMapping("/product")
    public String showProductDetails(Model model) {
        Product product = new Product(1, "Producto de ejemplo", "Descripción del producto", 19.99f, "vendedor", "producto.jpg", new java.util.ArrayList<>());
        model.addAttribute("cssfile", "product");
        return "product";
    }
}
