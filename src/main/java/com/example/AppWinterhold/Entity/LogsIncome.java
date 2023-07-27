package com.example.AppWinterhold.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "logsIncome")
public class LogsIncome {
    @Id
    @Column(name = "uuid")
    private String uuid;
    @Column(name = "source")
    private String source;
    @Column(name = "createdBy")
    private String createdBy;
    @Column(name = "nominal")
    private Double nominal;
    @Column(name = "total")
    private Double total;

    @Column(name = "transactionDate")
    private LocalDate transactionDate;

}
