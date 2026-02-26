package es.mqm.webapp.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.mqm.webapp.model.CategoryData;
import es.mqm.webapp.model.Product;

@Controller
public class SearchController {
    @GetMapping("/search")
    public String index(Model model, @RequestParam(required = false, value = "search") String searchParam,
            @RequestParam(required = false, value = "category") String category,
            @RequestParam(required = false, value = "date") String date,
            @RequestParam(required = false, value = "minPrice") String minPrice,
            @RequestParam(required = false, value = "maxPrice") String maxPrice,
            @RequestParam(required = false, value = "location") String location) {

        List<Product> products = new ArrayList<>();
        ArrayList<String> categories = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            products.add(new Product(i + 1, searchParam, "Descripcion", 50, "Vendedor" + (i + 1),
                    "product-400x600.png", categories));
        }

        model.addAttribute("cssfile", "search");
        model.addAttribute("loggedin", true);
        model.addAttribute("products", products);
        model.addAttribute("categories", CategoryData.CATEGORIES);
        model.addAttribute("search", searchParam);
        model.addAttribute("category", category);
        model.addAttribute("date", date);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("location", location);

        model.addAttribute("isTodas", category == null || category.equals("todas") || category.isEmpty());
        model.addAttribute("isRopa", "ropa".equals(category));
        model.addAttribute("isInformatica", "informatica".equals(category));
        model.addAttribute("isAutomoviles", "automoviles".equals(category));
        model.addAttribute("isElectrodomesticos", "electrodomesticos".equals(category));
        model.addAttribute("isLibros", "libros".equals(category));
        model.addAttribute("isOtros", "otros".equals(category));

        model.addAttribute("isDateAny", date == null || date.isEmpty());
        model.addAttribute("isDateHoy", "hoy".equals(date));
        model.addAttribute("isDate7", "7".equals(date));
        model.addAttribute("isDate30", "30".equals(date));

        return "search";
    }
}