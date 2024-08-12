package com.example.winterhold.service.abs;

import com.example.winterhold.dto.models.DataDTO;
import com.example.winterhold.dto.notification.NotificationDetailDto;
import com.example.winterhold.entity.Notification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface NotificationService {
    DataDTO<List<NotificationDetailDto>> retriveNotification(Integer page);

    Integer getNotification();
    Notification sendNotification(UUID uuid, String membershipNumber, String header, String message, LocalDateTime date, String currentLogin);
}
