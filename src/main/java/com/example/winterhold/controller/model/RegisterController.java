package com.example.winterhold.controller.model;

import com.example.winterhold.dto.account.AccountInsertDto;
import com.example.winterhold.service.abs.AccountService;
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

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController extends BaseController {

    private final AccountService accountService;

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

    @GetMapping("/popup")
    public String popup(Model model) {

        String token = getToken();

        model.addAttribute("token", token);

        return "Register/profile";
    }

}
