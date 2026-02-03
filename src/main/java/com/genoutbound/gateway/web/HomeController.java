package com.genoutbound.gateway.web;

import java.time.OffsetDateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("appName", "Gen Outbound Gateway");
        model.addAttribute("now", OffsetDateTime.now());
        return "index";
    }
}
