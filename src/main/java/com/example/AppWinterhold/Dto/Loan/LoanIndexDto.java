package com.example.AppWinterhold.Dto.Loan;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class LoanIndexDto {

    private Long id; /*nn*/

    private String customerName;/*20 nn*/

    private String bookTitle;

    private LocalDate loanDate;/*nn*/

    private LocalDate dueDate;/*nn*/

    private LocalDate returnDate;/*n*/

    private String note;/*500 n*/

    private String dayLeft;
    private String lateLoan;
    private Long denda;

    public LoanIndexDto(Long id, String customerName, String bookTitle, LocalDate loanDate, LocalDate dueDate, LocalDate returnDate, String note, Long denda) {
        this.id = id;
        this.customerName = customerName;
        this.bookTitle = bookTitle;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.note = note;
        this.denda = denda;

    }
}
