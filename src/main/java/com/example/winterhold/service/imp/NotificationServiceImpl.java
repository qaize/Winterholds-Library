package com.example.winterhold.service.imp;

import com.example.winterhold.Dto.CurrentLoginDetailDTO;
import com.example.winterhold.Dto.notification.NotificationDetailDto;
import com.example.winterhold.controller.model.BaseController;
import com.example.winterhold.Dao.NotificationRepository;
import com.example.winterhold.Dto.Models.DataDTO;
import com.example.winterhold.Entity.Notification;
import com.example.winterhold.service.abs.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    private final static Logger LOGGER = LoggerFactory.getLogger(NotificationServiceImpl.class);

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
                    log.info("date {}", viewednotification.getCreatedDate());
                    Date date = null;
                    if (viewednotification.getCreatedDate().toString().length() < 20) {
                        date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(viewednotification.getCreatedDate().toString());
                    } else {
                        date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(viewednotification.getCreatedDate().toString());
                    }

                    String dateNotif = new SimpleDateFormat("dd MMMM yyyy HH:mm").format(date);

                    notificationDetailDtos.add(new NotificationDetailDto().builder()
                            .type(viewednotification.getNotificationHeader())
                            .dateRequest(dateNotif)
                            .requester(viewednotification.getRecipientId())
                            .message(viewednotification.getNotificationMessage())
                            .build());

                    viewednotification.setIsViewed(true);
                    viewednotification.setIsNew(false);
                }
                notificationRepository.saveAll(fetchedNotification);
            } else {
                fetchedNotification = new ArrayList<>();
            }

            return DataDTO.<List<NotificationDetailDto>>builder()
                    .data(notificationDetailDtos)
                    .totalPage((long) dataNotification.getTotalPages())
                    .message(message)
                    .flag(flag)
                    .build();

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }


    public Integer getNotification() {
        CurrentLoginDetailDTO currentLoginDetailDTO = new BaseController().getCurrentLoginDetail();

        Integer newNotification;

        String currentLogin = currentLoginDetailDTO.getRole().contains("customer") ? currentLoginDetailDTO.getUsername() : "admin";

        newNotification = notificationRepository.countByRecipientIdAndIsNew(currentLogin, true);


        return newNotification;
    }

    public Notification mapNotification(UUID uuid, String membershipNumber, String header, String message, LocalDateTime date, String currentLogin) {
        return new Notification(uuid.toString(), membershipNumber, false, true, header, message, date, currentLogin);
    }
}
