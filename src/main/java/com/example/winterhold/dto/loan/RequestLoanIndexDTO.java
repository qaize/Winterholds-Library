package com.example.winterhold.dto.loan;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
@Setter
public class RequestLoanIndexDTO {

    private Long requestId;
    private String fullName;
    private String bookTitle;
    private LocalDateTime requestDate;
    private String status;

    public RequestLoanIndexDTO(Long requestId, String fullName, String bookTitle,LocalDateTime requestDate, Boolean status) {
        this.requestId = requestId;
        this.fullName = fullName;
        this.bookTitle = bookTitle;
        this.requestDate = requestDate;
        this.status = status ? "Accepted" : "Pending";
    }

}
