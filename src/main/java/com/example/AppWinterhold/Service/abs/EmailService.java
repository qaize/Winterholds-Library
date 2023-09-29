package com.example.AppWinterhold.Service.abs;

import com.example.AppWinterhold.Dto.Mail.MailDTO;
import com.example.AppWinterhold.Entity.Mail;

public interface EmailService {

    String sendSimpleMail(Mail details);
    String sendMessage(MailDTO request);

    String sendMailWithAttachment(Mail details);
    String sendMessageWithAttachment(MailDTO details);
}
