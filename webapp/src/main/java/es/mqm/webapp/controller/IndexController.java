package es.mqm.webapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.mqm.webapp.model.CategoryData;
import es.mqm.webapp.model.Product;
import es.mqm.webapp.repository.ProductRepository;

@Controller
public class IndexController {

    @Autowired 
    private ProductRepository repository;

    @GetMapping("/")
    public String index(Model model) {

        List<Product> products = repository.findAll();

        model.addAttribute("cssfile", "index");
        model.addAttribute("loggedin", true);
        model.addAttribute("products", products);
        model.addAttribute("categories", CategoryData.CATEGORIES);
        return "index";
    }
}