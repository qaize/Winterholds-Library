package com.example.winterhold.service.abs;

import com.example.winterhold.dto.mail.MailDTO;
import com.example.winterhold.entity.Mail;

public interface EmailService {

    String sendSimpleMail(Mail details);
    String sendMessage(MailDTO request);

    String sendMailWithAttachment(Mail details);
    String sendMessageWithAttachment(MailDTO details);
}
