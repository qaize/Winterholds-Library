package com.example.winterhold.controller.model;

import com.example.winterhold.service.abs.NotificationService;
import com.example.winterhold.service.imp.LoanServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final NotificationService notificationService;

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("newNotification",notificationService.getNotification());
        return "Home/index";
    }
}
