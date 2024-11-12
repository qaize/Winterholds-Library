package com.example.winterhold.controller.model;


import com.example.winterhold.dto.BaseResponseDTO;
import com.example.winterhold.dto.payment.TopUpDTO;
import com.example.winterhold.service.abs.NotificationService;
import com.example.winterhold.service.abs.PaymentService;
import com.example.winterhold.service.abs.TopUpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final TopUpService topUpService;
    private final NotificationService notificationService;

    @GetMapping("/topup")
    public String topup(Model model) {
        model.addAttribute("newNotification",notificationService.getNotification());
        model.addAttribute("topupDTO",new TopUpDTO());
        return "Payment/topup";
    }

    @PostMapping("/topup")
    public String topup(@Valid @ModelAttribute TopUpDTO topupDTO, BindingResult bindingResult, Model model ) {
        if (bindingResult.hasErrors()) {
            return "Payment/topup";
        }

        String pages = "Payment/valid";

        BaseResponseDTO<Object> response = topUpService.topup(topupDTO);
        if(Objects.nonNull(response.getData())){
            model.addAttribute("newNotification",notificationService.getNotification());
            pages =  "Home/index";
        }else {

            model.addAttribute("validationHeader", "error dah gtw knp");
            model.addAttribute("validationReason", "Please, contact admin!");
            model.addAttribute("flag", 1);
        }

        return pages;

    }


}
