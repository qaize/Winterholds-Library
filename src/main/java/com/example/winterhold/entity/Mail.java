package com.example.winterhold.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Data
public class Mail {
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;

    public Mail(String recipient, String msgBody, String subject) {
        this.recipient = recipient;
        this.msgBody = msgBody;
        this.subject = subject;
    }
}
