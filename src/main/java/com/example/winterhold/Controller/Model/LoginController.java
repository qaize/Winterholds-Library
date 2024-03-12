package com.example.winterhold.Controller.Model;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping("/loginForm")
    public String loginForm() {
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
