package com.example.AppWinterhold.Security;

import com.example.AppWinterhold.Entity.Account;
import com.example.AppWinterhold.Service.abs.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthencticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private AccountService accountService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        var username = request.getParameter("username");
        Account data = accountService.getAccount(username);
        data.setCountWrong(0);
        accountService.setCountWrong(data);

        String link = "/home/index";
        response.sendRedirect(request.getContextPath() + link);
    }
}
