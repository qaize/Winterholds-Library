package com.example.winterhold.controller.model;

import com.example.winterhold.constants.ActionConstants;
import com.example.winterhold.dto.BaseResponseDTO;
import com.example.winterhold.dto.CurrentLoginDetailDTO;
import com.example.winterhold.dto.account.AccountInsertDto;
import com.example.winterhold.dto.customer.CustomerProfileDto;
import com.example.winterhold.service.abs.AccountService;
import com.example.winterhold.service.abs.NotificationService;
import com.example.winterhold.service.abs.PaymentService;
import com.example.winterhold.utility.Dropdown;
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
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController extends BaseController {

    private final AccountService accountService;
    private final PaymentService paymentService;
    private final NotificationService notificationService;

    @GetMapping("/registerForm")
    public String signup(Model model) {

        AccountInsertDto accountDto = new AccountInsertDto();
        model.addAttribute("dto", accountDto);
        model.addAttribute("roleList", Dropdown.dropdownRole());

        return "Register/registerForm";
    }

    @PostMapping("/registerForm")
    public String signup(@Valid @ModelAttribute("dto") AccountInsertDto dto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("dto", dto);
            model.addAttribute("roleList", Dropdown.dropdownRole());
            return "Register/registerForm";
        } else {

            accountService.insert(dto);

            return "redirect:/login/loginForm";
        }
    }

    @GetMapping("/enableBilling")
    public String enableBilling(Model model){

        BaseController baseController = new BaseController();
        CurrentLoginDetailDTO currentLogin = baseController.getCurrentLoginDetail();
        BaseResponseDTO<Object> response = paymentService.enablePayment(currentLogin.getUsername());
        String returnValue =  "Register/Valid";

        if(Objects.nonNull(response.getData())){
            model.addAttribute("membershipNumber", currentLogin.getRole());
            model.addAttribute("memberDto",(CustomerProfileDto) response.getData());
            model.addAttribute("newNotification",notificationService.getNotification());

            returnValue = "Customer/Detail";
        } else {
            model.addAttribute("validationHeader", "Failed enable payment: ".concat(response.getMessage()));
            model.addAttribute("validationReason", "Please, contact admin!");
            model.addAttribute("flag", 1);
        }

        return returnValue;
    }

    @GetMapping("/popup")
    public String popup(Model model) {

        String token = getToken();

        model.addAttribute("token", token);

        return "Register/profile";
    }



}
