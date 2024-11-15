package com.example.winterhold.dto.loan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class RequestLoanDTO {
    public String membershipNumber;
    public String bookCode;
}
