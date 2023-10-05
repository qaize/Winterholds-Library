package com.example.AppWinterhold.Service.imp;

import com.example.AppWinterhold.Controller.Model.BaseController;
import com.example.AppWinterhold.Dao.NotificationRepository;
import com.example.AppWinterhold.Dto.Models.DataDTO;
import com.example.AppWinterhold.Entity.Notification;
import com.example.AppWinterhold.Service.abs.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.AppWinterhold.Const.actionConst.INDEX_EMPTY;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final static Logger LOGGER = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public DataDTO<List<Notification>> getNotificationByCurrentLogin(Integer page) {

        String currentLogin = new BaseController().getCurrentLogin();

        String message = "";
        int flag = 0;

        try {
            Pageable pagination = PageRequest.of(page - 1, 5, Sort.by("createdDate").descending());
            Page<Notification> dataNotification = notificationRepository.findNotifcationByCurrentLogin(currentLogin,pagination);

            if (dataNotification.getContent().isEmpty()) {
                message = INDEX_EMPTY;
                flag = 1;
            }

            List<Notification> fetchedNotification = dataNotification.getContent();
            if (!fetchedNotification.isEmpty()) {
                for (Notification viewednotification : fetchedNotification
                ) {
                    viewednotification.setIsViewed(true);
                    viewednotification.setIsNew(false);
                }
                notificationRepository.saveAll(fetchedNotification);
            }else {
                fetchedNotification = new ArrayList<>();
            }

            return DataDTO.<List<Notification>>builder()
                    .data(fetchedNotification)
                    .totalPage((long) dataNotification.getTotalPages())
                    .message(message)
                    .flag(flag)
                    .build();

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }
}
