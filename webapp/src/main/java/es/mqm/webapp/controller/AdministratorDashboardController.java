package es.mqm.webapp.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.mqm.webapp.model.Image;
import es.mqm.webapp.model.Product;
import es.mqm.webapp.model.Review;
import es.mqm.webapp.model.User;
import es.mqm.webapp.service.*;

@Controller
public class AdministratorDashboardController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ReviewService reviewService;

    private static final int PAGE_SIZE = 5;

    private List<User> users = new ArrayList<User>();
    private List<Product> products = new ArrayList<Product>();
    private List<Review> reviews = new ArrayList<Review>();
    private long userCounter;
    private long productCounter;
    private long productSoldCounter;
    private List<Integer> categoriesSold = new ArrayList<Integer>();
    private List<Integer> reviewsRating = new ArrayList<Integer>();
    private List<Integer> newUsersPerMonth = new ArrayList<Integer>();

    @GetMapping("/administrator_dashboard")
    public String showAdministratorDashboardPage(Model model, @RequestParam(value = "pageUser", defaultValue = "0") int pageUser, @RequestParam(value = "pageProduct", defaultValue = "0") int pageProduct, @RequestParam(value = "pageReview", defaultValue = "0") int pageReview) {
        users.clear();
        products.clear();
        reviews.clear();
        categoriesSold.clear();
        reviewsRating.clear();
        newUsersPerMonth.clear();
        if (pageProduct < 0)
            pageProduct = 0;
        if (pageUser < 0)
            pageUser = 0;
        if (pageReview < 0)
            pageReview = 0;

        Page<Product> productPage = productService.findAll(PageRequest.of(pageProduct, PAGE_SIZE));
        Page<User> userPage = userService.findAll(PageRequest.of(pageUser, PAGE_SIZE));
        Page<Review> reviewPage = reviewService.findAll(PageRequest.of(pageReview, PAGE_SIZE));
        userCounter = userService.count();
        productCounter = productService.count();
        productSoldCounter = (long) (Math.floor(Math.random() * (productCounter)) + 1);
        
        for(int i=0;i<5;i++){
            categoriesSold.add((int) Math.floor(Math.random() * (250 - 50 + 1)) + 50);
        }
        for(int i=0;i<12;i++){
            newUsersPerMonth.add((int) Math.floor(Math.random() * (1000 - 200 + 1)) + 200);
        }
        for(int i=0;i<5;i++){
            reviewsRating.add((int) Math.floor(Math.random() * (200 - 30 + 1)) + 30);
        }

        if (pageProduct >= productPage.getTotalPages() && productPage.getTotalPages() > 0) {
            pageProduct = productPage.getTotalPages() - 1;
            productPage = productService.findAll(PageRequest.of(pageProduct, PAGE_SIZE));
        }
        if (pageUser >= userPage.getTotalPages() && userPage.getTotalPages() > 0) {
            pageUser = userPage.getTotalPages() - 1;
            userPage = userService.findAll(PageRequest.of(pageUser, PAGE_SIZE));
        }
        if (pageReview >= reviewPage.getTotalPages() && reviewPage.getTotalPages() > 0) {
            pageReview = reviewPage.getTotalPages() - 1;
            reviewPage = reviewService.findAll(PageRequest.of(pageReview, PAGE_SIZE));
        }
        int totalPagesProduct = productPage.getTotalPages() == 0 ? 1 : productPage.getTotalPages();
        int totalPagesUser = userPage.getTotalPages() == 0 ? 1 : userPage.getTotalPages();
        int totalPagesReview = reviewPage.getTotalPages() == 0 ? 1 : reviewPage.getTotalPages();

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("reviews", reviewPage.getContent());

        model.addAttribute("currentPageUser", pageUser+1);
        model.addAttribute("currentPageProduct", pageProduct+1);
        model.addAttribute("currentPageReview", pageReview+1);
        // 0-based values for pagination links
        model.addAttribute("currentPageUserZero", pageUser);
        model.addAttribute("currentPageProductZero", pageProduct);
        model.addAttribute("currentPageReviewZero", pageReview);
        model.addAttribute("totalPagesUser", totalPagesUser);
        model.addAttribute("totalPagesProduct", totalPagesProduct);
        model.addAttribute("totalPagesReview", totalPagesReview);
        model.addAttribute("hasPrevUser", userPage.hasPrevious());
        model.addAttribute("hasNextUser", userPage.hasNext());
        model.addAttribute("hasPrevProduct", productPage.hasPrevious());
        model.addAttribute("hasNextProduct", productPage.hasNext());
        model.addAttribute("hasPrevReview", reviewPage.hasPrevious());
        model.addAttribute("hasNextReview", reviewPage.hasNext());
        model.addAttribute("prevPageUser", pageUser - 1);
        model.addAttribute("nextPageUser", pageUser + 1);
        model.addAttribute("prevPageProduct", pageProduct - 1);
        model.addAttribute("nextPageProduct", pageProduct + 1);
        model.addAttribute("prevPageReview", pageReview - 1);
        model.addAttribute("nextPageReview", pageReview + 1);
        model.addAttribute("showPaginationUser", totalPagesUser > 1);
        model.addAttribute("showPaginationProduct", totalPagesProduct > 1);
        model.addAttribute("showPaginationReview", totalPagesReview > 1);

        model.addAttribute("userCounter", userCounter);
        model.addAttribute("productCounter", productCounter);
        model.addAttribute("productSoldCounter", productSoldCounter);
        String categoriesSoldJson = categoriesSold.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
        model.addAttribute("categoriesSoldJson", categoriesSoldJson);
        String reviewsRatingJson = reviewsRating.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
        model.addAttribute("reviewsRatingJson", reviewsRatingJson);
        String newUsersPerMonthJson = newUsersPerMonth.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
        model.addAttribute("newUsersPerMonthJson", newUsersPerMonthJson);

        model.addAttribute("cssfile", "administrator_dashboard"); 
        return "administrator_dashboard";
    }
}
