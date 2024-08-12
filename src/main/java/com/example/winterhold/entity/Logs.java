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
