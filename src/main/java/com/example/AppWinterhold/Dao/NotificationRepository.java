package com.example.AppWinterhold.Dao;

import com.example.AppWinterhold.Entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface NotificationRepository extends JpaRepository<Notification,String> {

    @Query("""
            select n
            from Notification n
            where n.recipientId = :currentLogin
            """)
    Page<Notification> findNotifcationByCurrentLogin(String currentLogin, Pageable pagination);
}
