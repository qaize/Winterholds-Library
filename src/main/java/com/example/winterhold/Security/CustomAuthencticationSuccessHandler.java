package com.example.winterhold.Security;

import com.example.winterhold.Entity.Account;
import com.example.winterhold.Service.abs.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthencticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private AccountService accountService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        var username = request.getParameter("username");
        String link = "/home/index";
        Account currentLogin = accountService.getAccount(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username : " + username + " not found"));
        currentLogin.setCountWrong(0);
        accountService.setCountWrong(currentLogin);


        response.sendRedirect(request.getContextPath() + link);
    }
}
