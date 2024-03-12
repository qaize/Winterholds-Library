package com.example.winterhold.Service.abs;

import com.example.winterhold.Dto.Models.DataDTO;
import com.example.winterhold.Entity.Notification;

import java.util.List;

public interface NotificationService {
    DataDTO<List<Notification>> getNotificationByCurrentLogin(Integer page);

    DataDTO<List<Notification>> getNotificationFromUser(Integer page);
}
