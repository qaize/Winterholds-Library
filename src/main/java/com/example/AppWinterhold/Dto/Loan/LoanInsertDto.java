package com.example.AppWinterhold.Dto.Loan;

import com.example.AppWinterhold.Validation.DendaChecker;
import com.example.AppWinterhold.Validation.LoanBookChecker;
import com.example.AppWinterhold.Validation.LoanChecker;
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
@LoanBookChecker(message = "This user already loan the book",customer = "customerNumber",book="bookCode")
public class LoanInsertDto {
    private Long id; /*nn*/

    @NotBlank(message = "Please Select the customers")
    @NotNull(message = "Please Select the customers")
    @DendaChecker(message = "Customer ini belum membayar denda")
    @LoanChecker(message = "Customer sudah mencapai maksimum peminjaman")
    private String customerNumber;/*20 nn*/

    @NotBlank(message = "Please Select the Books")
    @NotNull(message = "Please select the books")
    private String bookCode;

    @Size(max = 500, message = "Maximum 500 char")
    private String note;/*500 n*/

    private Long denda;/*500 n*/
}
