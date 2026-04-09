package es.mqm.webapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.mqm.webapp.model.CategoryData;
import es.mqm.webapp.model.ExtendedProduct;
import es.mqm.webapp.model.Review;
import es.mqm.webapp.service.ProductService;
import es.mqm.webapp.service.ReviewService;
import es.mqm.webapp.model.User;

@Controller
public class IndexController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/")
    public String index(Model model, Authentication authentication) {

        User user = (User) model.getAttribute("currentUser");
        List<ExtendedProduct> products = productService.getAvailableProducts(0, 12, user).getContent();
        Map<Integer, Double> sellerAverageCache = new HashMap<>();
        List<Map<String, Object>> productsVm = new ArrayList<>();

        for (ExtendedProduct product : products) {
            Map<String, Object> item = new HashMap<>();
            item.put("p", product.getP());
            item.put("distance", product.getDistance());

            int sellerId = product.getP().getUser().getId();
            double averageRating = sellerAverageCache.computeIfAbsent(sellerId, id -> {
                List<Review> reviews = reviewService.findByUserDest(id);
                return reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);
            });

            item.put("averageRating", Math.round(averageRating * 10.0) / 10.0);
            addAverageStars(item, averageRating);
            productsVm.add(item);
        }

        model.addAttribute("cssfile", "index");
        model.addAttribute("loggedin", true);
        model.addAttribute("products", productsVm);
        model.addAttribute("categories", CategoryData.CATEGORIES);

        return "index";
    }

    private void addAverageStars(Map<String, Object> item, double averageRating) {
        int roundedRating = Math.max(0, Math.min(5, (int) Math.round(averageRating)));
        item.put("star1", roundedRating >= 1);
        item.put("star2", roundedRating >= 2);
        item.put("star3", roundedRating >= 3);
        item.put("star4", roundedRating >= 4);
        item.put("star5", roundedRating >= 5);
    }
}
