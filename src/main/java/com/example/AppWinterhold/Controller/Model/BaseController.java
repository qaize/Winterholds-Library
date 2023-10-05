package com.example.AppWinterhold.Controller.Model;

import com.example.AppWinterhold.Dao.NotificationRepository;
import com.example.AppWinterhold.Dto.CurrentLoginDetailDTO;
import com.example.AppWinterhold.Entity.Notification;
import com.example.AppWinterhold.Service.imp.AccountServiceImp;
import com.example.AppWinterhold.Utility.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
public class BaseController {

    @Autowired
    JwtToken jwtToken;

    @Autowired
    AccountServiceImp account;


    public String getToken() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var username = authentication.getName();
        String token = jwtToken.generateToken("iwan", jwtToken.getSecret_key(), jwtToken.getAudience(), username);

        return token;
    }

    public String getCurrentLogin() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var username = authentication.getName();

        return username;
    }

    public CurrentLoginDetailDTO getCurrentLoginDetail() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CurrentLoginDetailDTO data = new CurrentLoginDetailDTO(authentication.getName(),authentication.getAuthorities().toString());

        return data;
    }


}
