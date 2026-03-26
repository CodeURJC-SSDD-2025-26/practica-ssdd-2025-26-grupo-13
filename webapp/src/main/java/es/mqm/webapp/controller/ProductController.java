package es.mqm.webapp.controller;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import es.mqm.webapp.model.User;
import es.mqm.webapp.service.UserService;
import es.mqm.webapp.model.Product;
import es.mqm.webapp.service.ProductService;
import es.mqm.webapp.model.Review;
import es.mqm.webapp.service.ReviewService;
import es.mqm.webapp.model.Image;

@Controller
public class ProductController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ReviewService reviewService;

    private static final int PAGE_SIZE = 3;
    private List<Review> reviews = new ArrayList<Review>();
    
    @GetMapping("/product/{id}")
    public String showProductDetails(@PathVariable("id") int id, Model model,@RequestParam(value = "pageReview", defaultValue = "0") int pageReview) {
        if (pageReview < 0) {
            pageReview = 0;
        }

        Product product = productService.findById(id).orElse(null);
        if (product == null) {
            return "redirect:/error";
        }
        Image image = product.getImage();
        if (image != null) {
            model.addAttribute("imageUrl", image.getId());
        } else {
            model.addAttribute("imageUrl", "product-400x600.png");
        }
        model.addAttribute("product", product);

        Page<Review> reviewPage = reviewService.findByProductId(id, PageRequest.of(pageReview, PAGE_SIZE));
        if (pageReview >= reviewPage.getTotalPages() && reviewPage.getTotalPages() > 0) {
            pageReview = reviewPage.getTotalPages() - 1;
            reviewPage = reviewService.findByProductId(id, PageRequest.of(pageReview, PAGE_SIZE));
        }

        boolean hasNextPage = reviewPage.hasNext();
        boolean hasPreviousPage = reviewPage.hasPrevious();
        int totalPagesReview = reviewPage.getTotalPages() == 0 ? 1 : reviewPage.getTotalPages();

        model.addAttribute("hasPrevPage", hasPreviousPage);
        model.addAttribute("hasNextPage", hasNextPage);
        model.addAttribute("next", pageReview + 1);
        model.addAttribute("previous", pageReview - 1);
        model.addAttribute("currentPageReview", pageReview + 1);
        model.addAttribute("totalPagesReview", totalPagesReview);
        model.addAttribute("reviews", reviewPage.getContent());
        model.addAttribute("showPaginationReview", totalPagesReview > 1);

        //reviews = reviewService.findByProductId(id);
        //model.addAttribute("reviews", reviews); 
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
        Product product = new Product("Producto", "buen estado","Descripcion", 50.0, user, "product-400x600.png", "informatica");
        model.addAttribute("cssfile", "sell_product");
        model.addAttribute("product", product);
        model.addAttribute("category", "informatica");
        return "modify_product";
    }
}
