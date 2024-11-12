package com.example.winterhold.dto.notification;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NotificationDetailDto {

    private String type;
    private String requester;
    private String message;
    private String dateRequest;

}
