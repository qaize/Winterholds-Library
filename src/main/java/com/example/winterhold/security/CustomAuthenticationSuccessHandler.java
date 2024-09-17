package com.example.winterhold.security;

import com.example.winterhold.entity.Account;
import com.example.winterhold.service.abs.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;


public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private AccountService accountService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        var username = request.getParameter("username");
        String link = "/home/index";
        Account currentLogin = accountService.getAccount(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username : " + username + " not found"));
        currentLogin.setCountWrong(0);
        accountService.setCountWrong(currentLogin);


        response.sendRedirect(request.getContextPath() + link);
    }
}
