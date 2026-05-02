package es.mqm.webapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.mqm.webapp.model.Order;
import es.mqm.webapp.model.Product;
import es.mqm.webapp.model.Review;
import es.mqm.webapp.model.User;
import es.mqm.webapp.dto.AdminChartsDTO;
import es.mqm.webapp.service.*;

@Controller
public class AdministratorDashboardController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ChartsService chartsService;

    private static final int PAGE_SIZE = 5;

    private List<User> users = new ArrayList<User>();
    private List<Product> products = new ArrayList<Product>();
    private List<Review> reviews = new ArrayList<Review>();
    private List<Order> orders = new ArrayList<Order>();
    private long userCounter;
    private long productCounter;
    private long productSoldCounter;
    private long orderCounter;

    private void addReviewStars(Map<String, Object> item, Review review) {
        int rating = Math.max(0, Math.min(5, Math.round(review.getRating())));
        item.put("star1", rating >= 1);
        item.put("star2", rating >= 2);
        item.put("star3", rating >= 3);
        item.put("star4", rating >= 4);
        item.put("star5", rating >= 5);
    }


    @GetMapping("/admin")
    public String showAdministratorDashboardPage(Model model, @RequestParam(value = "pageUser", defaultValue = "0") int pageUser, @RequestParam(value = "pageProduct", defaultValue = "0") int pageProduct, @RequestParam(value = "pageReview", defaultValue = "0") int pageReview, @RequestParam(value = "pageOrder", defaultValue = "0") int pageOrder) {
        users.clear();
        products.clear();
        reviews.clear();
        orders.clear();
        if (pageProduct < 0)
            pageProduct = 0;
        if (pageUser < 0)
            pageUser = 0;
        if (pageReview < 0)
            pageReview = 0;
        if (pageOrder < 0)
            pageOrder = 0;

        Page<Product> productPage = productService.findAll(PageRequest.of(pageProduct, PAGE_SIZE));
        Page<User> userPage = userService.findAll(PageRequest.of(pageUser, PAGE_SIZE));
        Page<Review> reviewPage = reviewService.findAll(PageRequest.of(pageReview, PAGE_SIZE));
        Page<Order> orderPage = orderService.findAll(PageRequest.of(pageOrder, PAGE_SIZE));
        productSoldCounter = orderService.count();

        AdminChartsDTO chartsData = chartsService.getAdminChartsData();
        userCounter = chartsData.userCounter();
        productCounter = chartsData.productCounter();
        orderCounter = chartsData.orderCounter();

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
        if(pageOrder >= orderPage.getTotalPages() && orderPage.getTotalPages() > 0){
            pageOrder = orderPage.getTotalPages() - 1;
            orderPage = orderService.findAll(PageRequest.of(pageOrder, PAGE_SIZE));
        }
        int totalPagesProduct = productPage.getTotalPages() == 0 ? 1 : productPage.getTotalPages();
        int totalPagesUser = userPage.getTotalPages() == 0 ? 1 : userPage.getTotalPages();
        int totalPagesReview = reviewPage.getTotalPages() == 0 ? 1 : reviewPage.getTotalPages();
        int totalPagesOrder = orderPage.getTotalPages() == 0 ? 1 : orderPage.getTotalPages();

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("orders", orderPage.getContent());
        List<Map<String, Object>> reviewsVm = new ArrayList<>();
        for (Review review : reviewPage.getContent()) {
            Map<String, Object> item = new HashMap<>();
            item.put("review", review);
            addReviewStars(item, review);
            reviewsVm.add(item);
        }
        model.addAttribute("reviewsVm", reviewsVm);

        model.addAttribute("currentPageUser", pageUser+1);
        model.addAttribute("currentPageProduct", pageProduct+1);
        model.addAttribute("currentPageReview", pageReview+1);
        model.addAttribute("currentPageOrder", pageOrder+1);
        // 0-based values for pagination links
        model.addAttribute("currentPageUserZero", pageUser);
        model.addAttribute("currentPageProductZero", pageProduct);
        model.addAttribute("currentPageReviewZero", pageReview);
        model.addAttribute("currentPageOrderZero",pageOrder);
        model.addAttribute("totalPagesUser", totalPagesUser);
        model.addAttribute("totalPagesProduct", totalPagesProduct);
        model.addAttribute("totalPagesReview", totalPagesReview);
        model.addAttribute("totalPagesOrder", totalPagesOrder);
        model.addAttribute("hasPrevUser", userPage.hasPrevious());
        model.addAttribute("hasNextUser", userPage.hasNext());
        model.addAttribute("hasPrevProduct", productPage.hasPrevious());
        model.addAttribute("hasNextProduct", productPage.hasNext());
        model.addAttribute("hasPrevReview", reviewPage.hasPrevious());
        model.addAttribute("hasNextReview", reviewPage.hasNext());
        model.addAttribute("hasPrevOrder", orderPage.hasPrevious());
        model.addAttribute("hasNextOrder", orderPage.hasNext());
        model.addAttribute("prevPageUser", pageUser - 1);
        model.addAttribute("nextPageUser", pageUser + 1);
        model.addAttribute("prevPageProduct", pageProduct - 1);
        model.addAttribute("nextPageProduct", pageProduct + 1);
        model.addAttribute("prevPageReview", pageReview - 1);
        model.addAttribute("nextPageReview", pageReview + 1);
        model.addAttribute("prevPageOrder", pageOrder - 1);
        model.addAttribute("nextPageOrder", pageOrder + 1);
        model.addAttribute("showPaginationUser", totalPagesUser > 1);
        model.addAttribute("showPaginationProduct", totalPagesProduct > 1);
        model.addAttribute("showPaginationReview", totalPagesReview > 1);
        model.addAttribute("showPaginationOrder", totalPagesOrder > 1);

        model.addAttribute("userCounter", userCounter);
        model.addAttribute("productCounter", productCounter);
        model.addAttribute("productSoldCounter", productSoldCounter);
        model.addAttribute("orderCounter", orderCounter);
        String categoriesSoldJson = chartsData.categoriesSold().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
        model.addAttribute("categoriesSoldJson", categoriesSoldJson);
        String reviewsRatingJson = chartsData.reviewsRating().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
        model.addAttribute("reviewsRatingJson", reviewsRatingJson);
        String newUsersPerMonthJson = chartsData.newUsersPerMonth().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
        model.addAttribute("newUsersPerMonthJson", newUsersPerMonthJson);

        model.addAttribute("cssfile", "administrator_dashboard");
        return "administrator_dashboard";
    }
    //There are different deletes for admins and users because the admin one redirect to the admin page
    @PostMapping("/admin/delete_user/{id}")
    public String deleteUser(@PathVariable int id, RedirectAttributes redirAttr){
        userService.deleteById(id);
        redirAttr.addFlashAttribute("toastMessage", "Usuario eliminado correctamente");
        return "redirect:/admin";
    }
    @PostMapping("/admin/delete_product/{id}")
    public String deleteProduct(@PathVariable int id, RedirectAttributes redirAttr){
        orderService.deleteByProductId(id);
        reviewService.deleteByProductId(id);
        productService.deleteById(id);
        redirAttr.addFlashAttribute("toastMessage", "Producto eliminado correctamente");
        return "redirect:/admin";
    }
    @PostMapping("/admin/delete_review/{id}")
    public String deleteReview(@PathVariable int id, RedirectAttributes redirAttr){
        reviewService.deleteById(id);
        redirAttr.addFlashAttribute("toastMessage", "Reseña eliminada correctamente");
        return "redirect:/admin";
    }

    @PostMapping("/admin/delete_order/{id}")
    public String deleteOrder(@PathVariable int id, RedirectAttributes redirAttr){
        orderService.deleteById(id);
        redirAttr.addFlashAttribute("toastMessage", "Pedido eliminado correctamente");
        return "redirect:/admin";
    }
}
