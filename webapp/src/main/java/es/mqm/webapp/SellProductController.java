package es.mqm.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SellProductController {
    @GetMapping("/sell_product")
     public String showSellProductPage(Model model) {
        model.addAttribute("cssfile", "sell_product");
        return "sell_product";
    }
}
