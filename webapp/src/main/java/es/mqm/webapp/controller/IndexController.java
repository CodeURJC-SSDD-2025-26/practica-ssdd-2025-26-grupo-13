package es.mqm.webapp.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.mqm.webapp.model.CategoryData;
import es.mqm.webapp.model.Product;

@Controller
public class IndexController {
    @GetMapping("/index")
    public String index(Model model) {

        List<Product> products = new ArrayList<Product>();
        ArrayList<String> categories = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            products.add(new Product(i+1, "Producto " + (i + 1),"Descripcion", 50, "Vendedor" + (i+1), "placeholder100x100.png", categories));
        }

        model.addAttribute("cssfile", "index");
        model.addAttribute("loggedin", true);
        model.addAttribute("products", products);
        model.addAttribute("categories", CategoryData.CATEGORIES);
        return "index";
    }
}