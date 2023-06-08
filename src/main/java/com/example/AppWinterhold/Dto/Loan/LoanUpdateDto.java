package com.example.AppWinterhold.Dto.Loan;

import com.example.AppWinterhold.Validation.TodayTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
}
