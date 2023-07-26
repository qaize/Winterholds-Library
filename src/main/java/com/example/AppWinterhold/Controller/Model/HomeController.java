package com.example.AppWinterhold.Controller.Model;


import com.example.AppWinterhold.Dao.AccountRepository;
import com.example.AppWinterhold.Entity.Account;
import com.example.AppWinterhold.Service.imp.AccountServiceImp;
//import com.example.AppWinterhold.kafka.KafkaProducer;
import liquibase.pro.packaged.K;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    AccountServiceImp account;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    String topicName = "newtopic";
//
//    public void sendMessage(String msg) {
//        kafkaTemplate.send(topicName, msg);
//    }


    @GetMapping("/index")
    public String index(Model model){

//        sendMessage("Home");

        model.addAttribute("userLogin",account.getCurrentUserLogin());
        return "Home/index";
    }
}
