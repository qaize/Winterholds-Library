package com.example.winterhold.service.abs;

import com.example.winterhold.Dto.Mail.MailDTO;
import com.example.winterhold.Entity.Mail;

public interface EmailService {

    String sendSimpleMail(Mail details);
    String sendMessage(MailDTO request);

    String sendMailWithAttachment(Mail details);
    String sendMessageWithAttachment(MailDTO details);
}
