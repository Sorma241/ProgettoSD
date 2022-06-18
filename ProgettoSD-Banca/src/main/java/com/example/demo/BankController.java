package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BankController {

    @RequestMapping("/")
    public String index() {
        return "index.html";
    }
    
    @RequestMapping("/transfer")
    public String transfer() {
        return "transfer.html";
    }
}
