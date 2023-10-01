package com.example.AppWinterhold.Controller.Model;

import com.example.AppWinterhold.Dto.Account.AccountInsertDto;
import com.example.AppWinterhold.Service.abs.AccountService;
import com.example.AppWinterhold.Utility.Dropdown;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegisterController extends BaseController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/registerForm")
    public String signup(Model model) {

        AccountInsertDto Accountdto = new AccountInsertDto();
        model.addAttribute("dto", Accountdto);
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
