package com.example.AppWinterhold.Service.abs;

import com.example.AppWinterhold.Entity.Mail;

public interface EmailService {
    // Method
    // To send a simple email
    String sendSimpleMail(Mail details);

    // Method
    // To send an email with attachment
    String sendMailWithAttachment(Mail details);
}
