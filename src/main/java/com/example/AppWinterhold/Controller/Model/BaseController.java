package com.example.AppWinterhold.Controller.Model;

import com.example.AppWinterhold.Dao.AccountRepository;
import com.example.AppWinterhold.Entity.Account;
import com.example.AppWinterhold.Service.imp.AccountServiceImp;
import com.example.AppWinterhold.Service.imp.AuthorServiceImp;
import com.example.AppWinterhold.Utility.JwtToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.Optional;

@Controller
public class BaseController {



    @Autowired
    JwtToken jwtToken;

    @Autowired
    AccountServiceImp account;

    public String getToken(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var username = authentication.getName();
        String token = jwtToken.generateToken("iwan",jwtToken.getSecret_key(),jwtToken.getAudience(),username);
        return token;
    }

    public String getCurrentLogin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var username = authentication.getName();



        return username;
    }

    public String getCurrentuser(){
    return account.getCurrentUserLogin();
    }
}
