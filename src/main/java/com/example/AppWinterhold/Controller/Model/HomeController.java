package com.example.AppWinterhold.Controller.Model;


import com.example.AppWinterhold.Service.imp.AccountServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    AccountServiceImp account;

    @GetMapping("/index")
    public String index(Model model) {

        model.addAttribute("userLogin", account.getCurrentUserLogin());

        return "Home/index";
    }
}
