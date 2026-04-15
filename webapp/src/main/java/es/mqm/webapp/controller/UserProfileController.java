package es.mqm.webapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import es.mqm.webapp.model.Image;
import es.mqm.webapp.model.Order;
import es.mqm.webapp.model.User;
import es.mqm.webapp.model.Product;
import es.mqm.webapp.model.Review;
import es.mqm.webapp.service.ReviewService;
import es.mqm.webapp.service.ProductService;
import es.mqm.webapp.service.OrderService;
import es.mqm.webapp.service.UserService;
import es.mqm.webapp.service.ReviewUtils;  

@Controller
public class UserProfileController {
    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ReviewService reviewService;

    private static final int PAGE_SIZE = 5;

    @GetMapping("/user_profile/{id}")
    public String showUserProfile(Model model, @PathVariable int id,@RequestParam(value = "pageOrder", defaultValue = "0") int pageOrder, @RequestParam(value = "pageProduct", defaultValue = "0") int pageProduct, @RequestParam(value = "pageReview", defaultValue = "0") int pageReview) {
        User user = userService.findById(id).orElse(null);
        if (user == null) {
            return "redirect:/error";
        }

        //for pagination
        if (pageProduct < 0)
            pageProduct = 0;
        if (pageOrder < 0)
            pageOrder = 0;
        if (pageReview < 0)
            pageReview = 0;

        Page<Product> productPage = productService.findByIsSoldFalseAndUser(user, PageRequest.of(pageProduct, PAGE_SIZE));
        Page<Order> orderPage = orderService.findByBuyer(user, PageRequest.of(pageOrder, PAGE_SIZE));
        Page<Review> reviewPage = reviewService.findByProductUserId(id, PageRequest.of(pageReview, PAGE_SIZE));
        if (pageProduct >= productPage.getTotalPages() && productPage.getTotalPages() > 0) {
            pageProduct = productPage.getTotalPages() - 1;
            productPage = productService.findByIsSoldFalseAndUser(user, PageRequest.of(pageProduct, PAGE_SIZE));
        }
        if (pageOrder >= orderPage.getTotalPages() && orderPage.getTotalPages() > 0) {
            pageOrder = orderPage.getTotalPages() - 1;
            orderPage = orderService.findByBuyer(user, PageRequest.of(pageOrder, PAGE_SIZE));
        }
        if (pageReview >= reviewPage.getTotalPages() && reviewPage.getTotalPages() > 0) {
            pageReview = reviewPage.getTotalPages() - 1;
            reviewPage = reviewService.findByProductUserId(id, PageRequest.of(pageReview, PAGE_SIZE));
        }
        int totalPagesProduct = productPage.getTotalPages() == 0 ? 1 : productPage.getTotalPages();
        int totalPagesOrder = orderPage.getTotalPages() == 0 ? 1 : orderPage.getTotalPages();
        int totalPagesReview = reviewPage.getTotalPages() == 0 ? 1 : reviewPage.getTotalPages();


        model.addAttribute("id", user.getId());
        model.addAttribute("name", user.getName());
        model.addAttribute("surnames", user.getSurnames());
        model.addAttribute("email", user.getEmail());
        User currentUser = (User) model.getAttribute("currentUser");
        boolean isUser = currentUser != null && currentUser.getId() == id;
        model.addAttribute("isUser", isUser);
        Image image = user.getImage();
        if (image != null) {
            model.addAttribute("imageUrl", image.getId());
        } else {
            model.addAttribute("imageUrl", "usuario anonimo.jpg");
        }
        model.addAttribute("location", user.getLocation().getName());
        int bought = orderService.countByBuyer(user);
        int sold = orderService.countBySeller(user);
        model.addAttribute("bought", bought);
        model.addAttribute("sold", sold);

        List<Product> products = productService.findByIsSoldFalseAndUser(user);
        List<Order> orders = orderService.findByBuyer(user);
        List<Review> reviews = reviewService.findByUserDest(id);
        Double average = reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);
        model.addAttribute("average", average);
        addAverageStars(model, average);

        //for pagination
        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("products", productPage.getContent());
        List<Map<String, Object>> reviewsVm = ReviewUtils.getReviewsVm(reviewPage, currentUser);
        model.addAttribute("reviewsVm", reviewsVm);

        model.addAttribute("currentPageOrder", pageOrder+1);
        model.addAttribute("currentPageProduct", pageProduct+1);
        model.addAttribute("currentPageReview", pageReview+1);
        // 0-based values for pagination links
        model.addAttribute("currentPageOrderZero", pageOrder);
        model.addAttribute("currentPageProductZero", pageProduct);
        model.addAttribute("currentPageReviewZero", pageReview);
        model.addAttribute("totalPagesOrder", totalPagesOrder);
        model.addAttribute("totalPagesProduct", totalPagesProduct);
        model.addAttribute("totalPagesReview", totalPagesReview);
        model.addAttribute("hasPrevOrder", orderPage.hasPrevious());
        model.addAttribute("hasNextOrder", orderPage.hasNext());
        model.addAttribute("hasPrevProduct", productPage.hasPrevious());
        model.addAttribute("hasNextProduct", productPage.hasNext());
        model.addAttribute("hasPrevReview", reviewPage.hasPrevious());
        model.addAttribute("hasNextReview", reviewPage.hasNext());
        model.addAttribute("prevPageOrder", pageOrder - 1);
        model.addAttribute("nextPageOrder", pageOrder + 1);
        model.addAttribute("prevPageProduct", pageProduct - 1);
        model.addAttribute("nextPageProduct", pageProduct + 1);
        model.addAttribute("prevPageReview", pageReview - 1);
        model.addAttribute("nextPageReview", pageReview + 1);
        model.addAttribute("showPaginationOrder", totalPagesOrder > 1);
        model.addAttribute("showPaginationProduct", totalPagesProduct > 1);
        model.addAttribute("showPaginationReview", totalPagesReview > 1);

        if(products.isEmpty()){
            model.addAttribute("emptyProducts", true);
        }else{
            model.addAttribute("emptyProducts", false);
        }

        if(orders.isEmpty()){
            model.addAttribute("emptyOrders", true);
        }else{
            model.addAttribute("emptyOrders", false);
        }

        if(reviews.isEmpty()){
            model.addAttribute("emptyReviews", true);
        }else{
            model.addAttribute("emptyReviews", false);
        }

        model.addAttribute("cssfile", "user_profile");
        return "user_profile";
    }

    private static void addAverageStars(Model model, double averageRating) {
        int roundedRating = Math.max(0, Math.min(5, (int) Math.round(averageRating)));
        model.addAttribute("avgStar1", roundedRating >= 1);
        model.addAttribute("avgStar2", roundedRating >= 2);
        model.addAttribute("avgStar3", roundedRating >= 3);
        model.addAttribute("avgStar4", roundedRating >= 4);
        model.addAttribute("avgStar5", roundedRating >= 5);
    }

}
