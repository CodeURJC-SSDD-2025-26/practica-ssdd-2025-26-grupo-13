package es.mqm.webapp.controller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;

import es.mqm.webapp.model.User;
import es.mqm.webapp.service.UserService;
import es.mqm.webapp.model.Product;
import es.mqm.webapp.service.ProductService;
import es.mqm.webapp.model.Review;
import es.mqm.webapp.service.ReviewService;
import es.mqm.webapp.model.ExtendedProduct;
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

    private static final int PAGE_SIZE = 3;
    
    @GetMapping("/product/{id}")
    public String showProductDetails(@PathVariable("id") int id, Model model,@RequestParam(value = "pageReview", defaultValue = "0") int pageReview) {
        if (pageReview < 0) {
            pageReview = 0;
        }

        User currentUser = (User) model.getAttribute("currentUser");
        ExtendedProduct extproduct = productService.findByIdWithDistance(id, currentUser).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        Product product = extproduct.getP();
        if (product.getImage() != null) {
            model.addAttribute("imageUrl", product.getImage().getId());
        } else {
            model.addAttribute("imageUrl", "product-400x600.png");
        }
        model.addAttribute("product", product);
        model.addAttribute("distance", extproduct.getDistance());

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
        model.addAttribute("next", hasNextPage ? pageReview + 1 : pageReview);
        model.addAttribute("previous", hasPreviousPage ? pageReview - 1 : pageReview);
        model.addAttribute("currentPageReview", pageReview + 1);
        model.addAttribute("totalPagesReview", totalPagesReview);
        model.addAttribute("reviews", reviewPage.getContent());
        model.addAttribute("showPaginationReview", totalPagesReview > 1);
        
        List<Review> reviews = reviewPage.getContent();
        List<Map<String, Object>> reviewsVm = new ArrayList<>();
        for (Review review : reviews) {
            Map<String, Object> item = new HashMap<>();
            item.put("review", review);
            boolean isUserReview = currentUser != null
                    && review.getUser() != null
                    && review.getUser().getId() == currentUser.getId();
            item.put("isUserReview", isUserReview);
            addReviewStars(item, review);
            reviewsVm.add(item);
        }
        model.addAttribute("reviewsVm", reviewsVm);
        
        model.addAttribute("cssfile", "product");
        return "product";
    }

    private void addReviewStars(Map<String, Object> item, Review review) {
        int rating = Math.max(0, Math.min(5, Math.round(review.getRating())));
        item.put("star1", rating >= 1);
        item.put("star2", rating >= 2);
        item.put("star3", rating >= 3);
        item.put("star4", rating >= 4);
        item.put("star5", rating >= 5);
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

    @PreAuthorize("@productService.isOwnerOrAdmin(#id, authentication)")
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
    @PostMapping("/modify_product")
    public String modifyProduct(Model model, @RequestParam int id, @RequestParam String name,
            @RequestParam String description, @RequestParam double price,
            @RequestParam(required = false) String state,
            @RequestParam(required=false) String category,
            @RequestParam(required = false) MultipartFile image)
            throws ResponseStatusException, IOException{
        Product product = productService.findById(id).orElse(null);
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        if (state != null && !state.isBlank()) {
            product.setState(state);
        }
        if (category != null && !category.isBlank()) {
            product.setCategory(category);
        }
        if (image != null && !image.isEmpty()) {
            Image im = imageService.createImage(image);
            productService.addImageToProduct(id, im);
        }
        productService.save(product);

        return "redirect:/product/" + id;            
    }
}
