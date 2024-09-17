package com.example.winterhold.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "logs")
public class Logs {

    @Id
    @Column(name = "uuid")
    private String uuid;

    @Column(name = "process")
    private String process;
    @Column(name = "status")
    private String status;
    @Column(name = "createdBy")
    private String createdBy;
    @Column(name = "process_date")
    private LocalDateTime date;
    @Column(name = "action_name")
    private String action;

}
