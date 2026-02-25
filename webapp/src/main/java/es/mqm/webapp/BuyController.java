package es.mqm.webapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BuyController {
    @GetMapping("/buy")
    public String buy(Model model) {

        model.addAttribute("cssfile", "buy");
        model.addAttribute("loggedin", true);
        return "buy";
    }
}