package com.example.AppWinterhold.Service.abs;

import com.example.AppWinterhold.Entity.Mail;

public interface EmailService {

    String sendSimpleMail(Mail details);

    String sendMailWithAttachment(Mail details);
}
