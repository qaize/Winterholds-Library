package com.example.AppWinterhold.Controller.Model;

import com.example.AppWinterhold.Dto.Dropdown.DropdownDto;
import com.example.AppWinterhold.Utility.Dropdown;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping("/loginForm")
    public String loginForm(Model model) {

        model.addAttribute("listRole", Dropdown.dropdownRole());
        return "Login/LoginForm";
    }

    @GetMapping("/accessDenied")
    public String accessDenied() {
        return "Login/access-denied";
    }

    @GetMapping("/loginFailed")
    public String loginError() {
        return "Login/login-failed";
    }


}
