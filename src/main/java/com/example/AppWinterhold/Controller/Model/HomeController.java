package com.example.AppWinterhold.Controller.Model;


import com.example.AppWinterhold.Service.imp.AccountServiceImp;
import com.example.AppWinterhold.Service.imp.LoanServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    AccountServiceImp account;

    @Autowired
    LoanServiceImp loanServiceImp;

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("newNotification",loanServiceImp.getNotification());
        return "Home/index";
    }
}
