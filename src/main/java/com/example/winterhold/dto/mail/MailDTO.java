package com.example.winterhold.dto.mail;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Getter @Setter
public class MailDTO {
    private String message;
    private String subject;
    private String receiver;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private MultipartFile[] attachment;
}
