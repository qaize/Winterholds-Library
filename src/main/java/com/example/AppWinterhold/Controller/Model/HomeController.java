package com.example.AppWinterhold.Controller.Model;


import com.example.AppWinterhold.Service.imp.AccountServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    AccountServiceImp account;
    String topicName = "newtopic";
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
//
//    public void sendMessage(String msg) {
//        kafkaTemplate.send(topicName, msg);
//    }

    @GetMapping("/index")
    public String index(Model model) {

//        sendMessage("Home");

        model.addAttribute("userLogin", account.getCurrentUserLogin());
        return "Home/index";
    }
}
