package com.example.AppWinterhold.Entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

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
    private LocalDate date;

}
