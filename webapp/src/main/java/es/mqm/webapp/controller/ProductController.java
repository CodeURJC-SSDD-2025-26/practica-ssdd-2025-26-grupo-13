package es.mqm.webapp.controller;
import java.util.ArrayList;

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
    @GetMapping("/sell_product")
     public String showSellProductPage(Model model) {
        model.addAttribute("cssfile", "sell_product");
        return "sell_product";
    }
    @GetMapping("/modify_product")
     public String showModifyProductPage(Model model) {
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Categoria 1");
        Product product = new Product(1,"Producto","Descripcion", 50.0, "Vendedor","product-400x600.png", categories);
        model.addAttribute("cssfile", "sell_product"); 
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        return "modify_product";
    }
}
