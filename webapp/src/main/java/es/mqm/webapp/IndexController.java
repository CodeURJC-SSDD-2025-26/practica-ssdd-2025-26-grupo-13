package es.mqm.webapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/index")
    public String index(Model model) {

        List<Product> products = new ArrayList<Product>();
        ArrayList<String> categories = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            products.add(new Product("Producto " + (i + 1), "", 50, "placeholder100x100.png", categories));
        }

        model.addAttribute("cssfile", "index");
        model.addAttribute("loggedin", true);
        model.addAttribute("products", products);
        model.addAttribute("categories", CategoryData.CATEGORIES);
        return "index";
    }
}