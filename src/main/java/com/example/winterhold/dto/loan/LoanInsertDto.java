package com.example.winterhold.dto.loan;

import com.example.winterhold.validation.DendaChecker;
import com.example.winterhold.validation.LoanBookChecker;
import com.example.winterhold.validation.LoanChecker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


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
