package com.example.winterhold.security;

import com.example.winterhold.entity.Account;
import com.example.winterhold.service.abs.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.util.Optional;


public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private AccountService accountService;


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        var link = "/login/loginFailed";
        String message = "";
        link = "/login/loginForm";

        var username = request.getParameter("username");
        var password = request.getParameter("password");
        var role = request.getParameter("role");

        if ((username == null || username.isBlank())) {

            message = "Username required";
            response.sendRedirect(request.getContextPath() + link + "?user=" + message);
        }
        else if((password == null || password.isBlank())){

            message = "password required";
            response.sendRedirect(request.getContextPath() + link + "?pasw=" + message);


        }
        else if((role == null || role.isBlank())){

            message = "role required";
            response.sendRedirect(request.getContextPath() + link + "?role=" + message);

        }
        else{
            Optional<Account> data = accountService.getAccount(username);

            if (data.isPresent()) {


                Integer count = data.get().getCountWrong() + 1;
                data.get().setCountWrong(count);
                if (data.get().getCountWrong() >= 3) {
                    message = "Akun terkunci";
                    data.get().setIslocked(true);
                } else {
                    message = "Password salah, sisa kesempatan = " + (3 - data.get().getCountWrong());
                }
                accountService.setCountWrong(data.get());
            }
            else{
                message = "User not found";
            }
            response.sendRedirect(request.getContextPath() + link + "?error=" + message);

        }

    }


}
