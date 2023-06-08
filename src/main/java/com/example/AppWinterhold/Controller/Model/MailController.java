package com.example.AppWinterhold.Controller.Model;

import com.example.AppWinterhold.Entity.Mail;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mail")
public class MailController {

    @GetMapping("/sendMail")
    public String sendMail(){
        return "Mail/MailSender";
    }
}
