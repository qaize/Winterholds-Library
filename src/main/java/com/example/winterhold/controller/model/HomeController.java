package com.example.winterhold.controller.model;

import com.example.winterhold.Service.imp.LoanServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final LoanServiceImp loanServiceImp;

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("newNotification",loanServiceImp.getNotification());
        return "Home/index";
    }
}
