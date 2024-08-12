package com.example.winterhold.controller.model;

import com.example.winterhold.dto.mail.MailDTO;
import com.example.winterhold.service.imp.EmailServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mail")
public class MailController {

    EmailServiceImp emailServiceImp;

    @Autowired
    public MailController(EmailServiceImp emailServiceImp) {

        this.emailServiceImp = emailServiceImp;
    }

    @GetMapping("/sendMail")
    public String sendMail() {
        return "Mail/MailSender";
    }


    @GetMapping("/send-message")
    public String insert(Model model) {

        MailDTO request = new MailDTO();

        model.addAttribute("dto", request);

        return "Mail/sendMessage";
    }

    @PostMapping("/send-message")
    public String insert(@Valid @ModelAttribute("dto") MailDTO dto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("dto", dto);
            return "Mail/sendMessage";
        } else {
            emailServiceImp.sendMessageWithAttachment(dto);

            return "redirect:/home/index";
        }
    }
}
