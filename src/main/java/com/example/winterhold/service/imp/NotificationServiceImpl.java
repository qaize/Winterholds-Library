package com.example.winterhold.service.imp;

import com.example.winterhold.dto.CurrentLoginDetailDTO;
import com.example.winterhold.dto.notification.NotificationDetailDto;
import com.example.winterhold.controller.model.BaseController;
import com.example.winterhold.repository.NotificationRepository;
import com.example.winterhold.dto.models.DataDTO;
import com.example.winterhold.entity.Notification;
import com.example.winterhold.service.abs.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.example.winterhold.constants.ActionConstants.INDEX_EMPTY;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {


    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    @Override
    public DataDTO<List<NotificationDetailDto>> retriveNotification(Integer page) {
        BaseController baseController = new BaseController();
        List<NotificationDetailDto> notificationDetailDtos = new ArrayList<>();
        String currentLogin = baseController.getCurrentLoginDetail().getRole().contains("customer") ? baseController.getCurrentLogin() : "admin";

        String message = "";
        int flag = 0;

        try {
            Pageable pagination = PageRequest.of(page - 1, 5, Sort.by("createdDate").descending());
            Page<Notification> dataNotification = notificationRepository.findNotifcationByCurrentLogin(currentLogin, pagination);

            if (dataNotification.getContent().isEmpty()) {
                message = INDEX_EMPTY;
                flag = 1;
            }

            List<Notification> fetchedNotification = dataNotification.getContent();
            if (!fetchedNotification.isEmpty()) {
                for (Notification viewednotification : fetchedNotification
                ) {
                    NotificationDetailDto data = populateNotification(viewednotification);
                    notificationDetailDtos.add(data);
                    viewednotification.setIsViewed(true);
                    viewednotification.setIsNew(false);
                }
                notificationRepository.saveAll(fetchedNotification);
            }

            return DataDTO.<List<NotificationDetailDto>>builder()
                    .data(notificationDetailDtos)
                    .totalPage((long) dataNotification.getTotalPages())
                    .message(message)
                    .flag(flag)
                    .build();

        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    private NotificationDetailDto populateNotification(Notification notification) throws ParseException {

        Date date = null;
        if (notification.getCreatedDate().toString().length() < 20) {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(notification.getCreatedDate().toString());
        } else {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(notification.getCreatedDate().toString());
        }

        String dateNotif = new SimpleDateFormat("dd MMMM yyyy HH:mm").format(date);

        return  NotificationDetailDto.builder()
                .type(notification.getNotificationHeader())
                .dateRequest(dateNotif)
                .requester(notification.getRecipientId())
                .message(notification.getNotificationMessage())
                .build();
    }


    public Integer getNotification() {
        CurrentLoginDetailDTO currentLoginDetailDTO = new BaseController().getCurrentLoginDetail();

        Integer newNotification;

        String currentLogin = currentLoginDetailDTO.getRole().contains("customer") ? currentLoginDetailDTO.getUsername() : "admin";

        newNotification = notificationRepository.countByRecipientIdAndIsNew(currentLogin, true);


        return newNotification;
    }

    public Notification sendNotification(UUID uuid, String membershipNumber, String header, String message, LocalDateTime date, String currentLogin) {
        return new Notification(uuid.toString(), membershipNumber, false, true, header, message, date, currentLogin);
    }
}
