package com.example.winterhold.dto.payment;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopUpDTO {
    private String membershipNumber;
    private Integer amount;
}
