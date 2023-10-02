package com.example.AppWinterhold.Dto.Loan;

import liquibase.pro.packaged.G;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
@Setter
public class RequestLoanIndexDTO {

    private String fullName;
    private String bookTitle;
    private LocalDateTime requestDate;
    private String status;

    public RequestLoanIndexDTO(String fullName, String bookTitle,LocalDateTime requestDate, Boolean status) {
        this.fullName = fullName;
        this.bookTitle = bookTitle;
        this.requestDate = requestDate;
        this.status = status ? "Accepted" : "Pending";
    }

}