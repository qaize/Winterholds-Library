package com.example.winterhold.Dto.Customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerProfileDto {

    private String membershipNumber; /*20 nn*/

    private String fullName;

    private String birthDate;/*nn*/

    private String phone;/*20 n*/

    private String address;/*500 n*/
    private String gender;/*500 n*/

}
