package com.example.winterhold.dao;

import com.example.winterhold.entity.Notification;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
@Transactional
public interface NotificationRepository extends JpaRepository<Notification,String> {

    Integer countByRecipientIdAndIsNew(String recipientId,Boolean isNew);

    @Query("""
            select n
            from Notification n
            where n.recipientId = :currentLogin
            """)
    Page<Notification> findNotifcationByCurrentLogin(String currentLogin, Pageable pagination);

    @Query("""
            select count(n)
            from Notification n
            where n.recipientId = :currentLogin AND n.isNew = 1
            """)
    Integer findNewNotification(String currentLogin);
}
