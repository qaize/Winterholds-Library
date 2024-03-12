package com.example.winterhold.Controller.Model;

import com.example.winterhold.Dto.Models.DataDTO;
import com.example.winterhold.Entity.Notification;
import com.example.winterhold.Service.abs.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping(value = "/show-notification-by-id")
    public String getNotificationById(Model model, @RequestParam(defaultValue = "1") Integer page){
        DataDTO<List<Notification>> data = notificationService.getNotificationByCurrentLogin(page);

        model.addAttribute("flag",data.getFlag());
        model.addAttribute("message",data.getMessage());
        model.addAttribute("totalPage",data.getFlag());
        model.addAttribute("currentPage",page);
        model.addAttribute("newNotification",0);
        model.addAttribute("notificationList",data.getData());
        return "Notification/Index";
    }


    @GetMapping(value = "/show-all-notification")
    public String getAllNotification(Model model, @RequestParam(defaultValue = "1") Integer page){
        DataDTO<List<Notification>> data = notificationService.getNotificationFromUser(page);

        model.addAttribute("flag",data.getFlag());
        model.addAttribute("message",data.getMessage());
        model.addAttribute("totalPage",data.getFlag());
        model.addAttribute("currentPage",page);
        model.addAttribute("newNotification",0);
        model.addAttribute("notificationList",data.getData());

        return "Notification/Index";
    }
}
