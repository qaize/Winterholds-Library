package com.example.winterhold.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
public class Notification {

    @Id
    @Column(name = "Id")
    private String id;
    @Column(name = "recipientId")
    private String recipientId;
    @Column(name = "isViewed")
    private Boolean isViewed;
    @Column(name = "isNew")
    private Boolean isNew;
    @Column(name = "notificationHeader")
    private String notificationHeader;
    @Column(name = "notificationMessage")
    private String notificationMessage;
    @Column(name = "createdDate")
    private LocalDateTime createdDate;
    @Column(name = "createdBy")
    private String createdBy;
}
