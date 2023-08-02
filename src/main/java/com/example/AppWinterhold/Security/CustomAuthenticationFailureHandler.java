package com.example.AppWinterhold.Security;

import com.example.AppWinterhold.Entity.Account;
import com.example.AppWinterhold.Service.abs.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private AccountService accountService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        var link = "/login/loginFailed";
        String message = "";

        var username = request.getParameter("username");
        var password = request.getParameter("password");

        if ((username == null || username.isBlank()) || (password == null || password.isBlank())) {

            message = "Username or Password Required";
        }

        Account data = accountService.getAccount(username);

        if (data != null) {
            Integer count = data.getCountWrong() + 1;
            data.setCountWrong(count);
            if (data.getCountWrong() >= 3) {
                message = "Akun terkunci";
                data.setIslocked(true);
            } else {
                message = "Password salah, sisa kesempatan = " + (3 - data.getCountWrong());
            }
            accountService.setCountWrong(data);
        } else {
            message = "Username / password salah";
        }

        link = "/login/loginForm";
        response.sendRedirect(request.getContextPath() + link + "?error=" + message);
    }


}
