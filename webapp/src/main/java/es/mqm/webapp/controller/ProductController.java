package es.mqm.webapp.controller;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import es.mqm.webapp.model.User;
import es.mqm.webapp.service.UserService;
import es.mqm.webapp.model.Product;
import es.mqm.webapp.service.ProductService;
import es.mqm.webapp.model.Review;
import es.mqm.webapp.service.ReviewService;
import es.mqm.webapp.model.Image;
import es.mqm.webapp.service.ImageService;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ImageService imageService;

    @GetMapping("/product/{id}")
    public String showProductDetails(@PathVariable("id") int id, Model model, Image image){
        Product product = productService.findById(id).orElse(null);
        if (product == null) {
            return "redirect:/error";
        }
        model.addAttribute("image",image);
        if (product.getImage() != null) {
            model.addAttribute("imageUrl", product.getImage().getId());
        } else {
            model.addAttribute("imageUrl", "product-400x600.png");
        }
        model.addAttribute("product", product);
        List<Review> reviews = reviewService.findByProductId(id);
        model.addAttribute("reviews", reviews);
        model.addAttribute("cssfile", "product");
        return "product";
    }

    @PostMapping("/newProduct")
    public String uploadProduct(
            Model model,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam double price,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) MultipartFile image
    ) throws IOException {

        User user = userService.findById(((User) model.getAttribute("currentUser")).getId()).orElse(null);
        if (user == null) {
            return "redirect:/error";
        }
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setState(state != null && !state.isBlank() ? state : "buen estado");
        product.setCategory(category != null && !category.isBlank() ? category : "informatica");
        product.setUser(user);

        if (image != null && !image.isEmpty()) {
            Image im = imageService.createImage(image);
            product.setImage(im);
        }

        productService.save(product);
        return "redirect:/product/" + product.getId();
    }

    @GetMapping("/sell_product")
    public String showSellProductPage(Model model) {
        model.addAttribute("cssfile", "sell_product");
        return "sell_product";
    }

    @GetMapping("/modify_product/{id}")
    public String showModifyProductPage(Model model, @PathVariable int id) {
        
        Optional<Product> product = productService.findById(id);
        if(product.isPresent()){
            model.addAttribute("product", product.get());
        }else{
            throw new RuntimeException("Failed to load default product image");
        }
        model.addAttribute("cssfile", "sell_product");
        
        return "modify_product";
    }
}
