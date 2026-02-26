package es.mqm.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ModifyProductController {
    @GetMapping("/modify_product")
     public String showModifyProductPage(Model model) {
        model.addAttribute("cssfile", "sell_product"); 
        return "modify_product";
    }
}
