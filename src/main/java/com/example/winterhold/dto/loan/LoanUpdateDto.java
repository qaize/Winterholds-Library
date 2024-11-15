package com.example.winterhold.dto.loan;

import com.example.winterhold.validation.TodayTime;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class LoanUpdateDto {
    @NotNull(message = "Id cannot null")
    private Long id; /*nn*/

    @NotNull(message = "Please Select the customers")
    private String customerNumber;/*20 nn*/

    @NotNull(message = "Please select the books")
    private String bookCode;

    @TodayTime(message = "Cannot insert date more than today!")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Loan Date cannot null")
    private LocalDate loanDate;/*nn*/

    @NotNull(message = "due date cannot null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;/*nn*/

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate returnDate;/*n*/

    @Size(max = 500, message = "Maximum 500 char")
    private String note;/*500 n*/

    private Long denda;/*500 n*/
}
