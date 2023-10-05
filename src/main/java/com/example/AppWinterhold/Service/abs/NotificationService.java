package com.example.AppWinterhold.Service.abs;

import com.example.AppWinterhold.Dto.Models.DataDTO;
import com.example.AppWinterhold.Entity.Notification;

import java.util.List;

public interface NotificationService {
    DataDTO<List<Notification>> getNotificationByCurrentLogin(Integer page);
}
