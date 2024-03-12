package com.example.winterhold.Dto.Loan;

import java.time.LocalDate;

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
    private String loanStatus;
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
