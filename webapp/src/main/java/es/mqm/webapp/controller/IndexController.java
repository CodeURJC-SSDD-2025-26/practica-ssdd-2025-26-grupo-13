package es.mqm.webapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.mqm.webapp.model.CategoryData;
import es.mqm.webapp.model.Product;
import es.mqm.webapp.service.ProductService;

@Controller
public class IndexController {

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String index(Model model) {

        // Show only the first 12 products on the homepage
        java.util.List<Product> products = productService.getProducts(0, 12).getContent();

        model.addAttribute("cssfile", "index");
        model.addAttribute("loggedin", true);
        model.addAttribute("products", products);
        model.addAttribute("categories", CategoryData.CATEGORIES);

        return "index";
    }
}