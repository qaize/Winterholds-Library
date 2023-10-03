package com.example.AppWinterhold.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "request")
@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
public class RequestLoan {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "membershipNumber")
    private String membershipNumber;

    @Column(name = "bookCode")
    private String bookCode;

    @Column(name = "requestDate")
    private LocalDateTime requestDate;

    @Column(name = "status")
    private Boolean status;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membershipNumber",insertable = false,updatable = false)
    private Customer customer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookCode",insertable = false,updatable = false)
    private Book book;


    public RequestLoan(Long id,String membershipNumber, String bookCode, LocalDateTime requestDate, Boolean status) {
        this.membershipNumber = membershipNumber;
        this.bookCode = bookCode;
        this.requestDate = requestDate;
        this.status = status;
        this.id = id;
    }
}
