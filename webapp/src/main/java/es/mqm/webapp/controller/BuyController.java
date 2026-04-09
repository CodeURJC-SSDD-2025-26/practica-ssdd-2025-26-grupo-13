package es.mqm.webapp.controller;

import es.mqm.webapp.service.MailService;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import es.mqm.webapp.model.Order;
import es.mqm.webapp.model.Product;
import es.mqm.webapp.model.User;
import es.mqm.webapp.service.OrderService;
import es.mqm.webapp.service.ProductService;
import es.mqm.webapp.service.TicketService;

@Controller
public class BuyController {

    @Autowired
    private MailService mailService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TicketService ticketService;

    BuyController(MailService mailService) {
        this.mailService = mailService;
    }

    @GetMapping("/buy/{id}")
    public String buy(Model model, @PathVariable int id) {

        Optional<Product> productOpt = productService.findById(id);
        if (!productOpt.isPresent() || productOpt.get().getIsSold()) {
            return "redirect:/";
        }
        Product product = productOpt.get();
        System.out.println(product);
        model.addAttribute("product", product);
        double price = product.getPrice();
        boolean priceLessThan30 = price < 30;
        model.addAttribute("priceLessThan30", priceLessThan30);
        model.addAttribute("priceWithShipping", priceLessThan30 ? price + 3.5 : price);
        model.addAttribute("cssfile", "buy");
        return "buy";
    }

    @PostMapping("/buy/{id}")
    public String completeBuy(Model model, @PathVariable int id, @RequestParam(value = "name") String name,
            @RequestParam(value = "surnames") String surnames,
            @RequestParam(value = "country") String country, @RequestParam(value = "address") String address,
            @RequestParam(value = "apartment", required=false) String apartment, @RequestParam(value = "province") String province,
            @RequestParam(value = "city") String city, @RequestParam(value = "zipcode") String zipcode,
            @RequestParam(value = "phone") String phone, @RequestParam(value = "creditCardNumber") String creditCardNumber,
            @RequestParam(value = "creditCardExpiryDate") String creditCardExpiryDate,
            @RequestParam(value = "creditCardCVV") String creditCardCVV, RedirectAttributes redirAttr) {

        Optional<Product> productOpt = productService.findById(id);
        if (!productOpt.isPresent() || productOpt.get().getIsSold()) {
            return "redirect:/";
        }
        Product product = productOpt.get();

        if (!zipcode.matches("\\d{5}")) {
            redirAttr.addFlashAttribute("zipcodeinvalid", true);
            return "redirect:/buy/" + id;
        }
        if (!creditCardExpiryDate.matches("^(0[1-9]|1[0-2])/\\d{2}$")) {
            redirAttr.addFlashAttribute("dateinvalid", true);
            return "redirect:/buy/" + id;
        }
        if (!creditCardCVV.matches("\\d{3}")) {
            redirAttr.addFlashAttribute("cvvinvalid", true);
            return "redirect:/buy/" + id;
        }

        User buyer = (User) model.getAttribute("currentUser");

        Order order = new Order(buyer, product, name, surnames, address, apartment == null ? "" : apartment, zipcode, city,
                province, country, phone, creditCardNumber, creditCardExpiryDate, creditCardCVV, product.getPrice() < 30 ? product.getPrice() + 3.5 : product.getPrice());
        orderService.save(order);
        product.setIsSold(true);
        productService.save(product);
        mailService.sendOrderConfirmation(order);
        return "redirect:/order_successful/" + order.getId();
    }

    @PreAuthorize("@orderService.isBuyerOrAdmin(#id, authentication)")
    @GetMapping("buy/{id}/ticket")
    public ResponseEntity<byte[]> downloadTicket(@PathVariable int id) {
        Order order = orderService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));
        byte[] ticketBytes = ticketService.generateTicket(order);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pedido_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(ticketBytes);
    }

    @PreAuthorize("@orderService.isBuyerOrAdmin(#id, authentication)")
    @GetMapping("/order_successful/{id}")
    public String showProductoComprado(Model model,@PathVariable int id) {
        Optional<Order> orderOpt = orderService.findById(id);
        if (!orderOpt.isPresent()) {
            return "redirect:/";
        }
        model.addAttribute("id", id);
        model.addAttribute("cssfile", "error");
        return "order_successful";
    }

}