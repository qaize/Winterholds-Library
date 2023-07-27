package com.example.AppWinterhold.Dto.Loan;

import com.example.AppWinterhold.Validation.DendaChecker;
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
@Getter
@Setter
public class LoanInsertDto {
    private Long id; /*nn*/

    @NotNull(message = "Please Select the customers")
    @NotBlank(message = "Please Select the customers")
    @DendaChecker(message = "Customer ini belum membayar denda")
    private String customerNumber;/*20 nn*/

    @NotBlank(message = "Please Select the Books")
    @NotNull(message = "Please select the books")
    private String bookCode;

    @TodayTime(message = "Cannot insert date more than today!")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Loan Date cannot null")
    private LocalDate loanDate;/*nn*/

    @Size(max = 500, message = "Maximum 500 char")
    private String note;/*500 n*/

    private Long denda;/*500 n*/
}
