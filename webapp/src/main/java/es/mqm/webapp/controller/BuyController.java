package es.mqm.webapp.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.mqm.webapp.model.Order;
import es.mqm.webapp.model.Product;
import es.mqm.webapp.model.User;
import es.mqm.webapp.service.OrderService;
import es.mqm.webapp.service.ProductService;

@Controller
public class BuyController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;  

    @GetMapping("/buy/{id}")
    public String buy(Model model, @PathVariable int id) {

        Optional<Product> productOpt = productService.findById(id);
        if (!productOpt.isPresent() || productOpt.get().getIsSold()) {
            return "redirect:/";
        }
        Product product = productOpt.get();
        System.out.println(product);
        model.addAttribute("product", product);
        model.addAttribute("priceWithShipping", product.getPrice() + 3.5);
        model.addAttribute("cssfile", "buy");
        return "buy";
    }

    @PostMapping("/buy/{id}")
    public String completeBuy(Model model, @PathVariable int id, @RequestParam(value = "name") String name,
            @RequestParam(value = "surnames") String surnames,
            @RequestParam(value = "country") String country, @RequestParam(value = "address") String address,
            @RequestParam(value = "apartment") String apartment, @RequestParam(value = "province") String province,
            @RequestParam(value = "city") String city, @RequestParam(value = "zipcode") int zipcode,
            @RequestParam(value = "phone") String phone, @RequestParam(value = "creditCardNumber") int creditCardNumber,
            @RequestParam(value = "creditCardExpiryDate") String creditCardExpiryDate,
            @RequestParam(value = "creditCardCVV") int creditCardCVV) {

        Optional<Product> productOpt = productService.findById(id);
        if (!productOpt.isPresent() || productOpt.get().getIsSold()) {
            return "redirect:/";
        }
        Product product = productOpt.get();

        User buyer = (User) model.getAttribute("currentUser");

        Order order = new Order(buyer, product, name, surnames, address, apartment, zipcode, city,
                province, phone, creditCardNumber, creditCardExpiryDate, creditCardCVV);
        orderService.save(order);
        product.setIsSold(true);
        productService.save(product);
        return "redirect:/";
    }
}