package com.example.winterhold.service.abs;

import com.example.winterhold.Dto.Models.DataDTO;
import com.example.winterhold.Dto.notification.NotificationDetailDto;
import com.example.winterhold.Entity.Notification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface NotificationService {
    DataDTO<List<NotificationDetailDto>> retriveNotification(Integer page);

    Integer getNotification();
    Notification mapNotification(UUID uuid, String membershipNumber, String header, String message, LocalDateTime date, String currentLogin);
}
