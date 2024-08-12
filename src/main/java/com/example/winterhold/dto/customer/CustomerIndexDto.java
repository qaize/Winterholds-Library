package com.example.winterhold.dto.customer;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class CustomerIndexDto {
    private String membershipNumber; /*20 nn*/

    private String firstName;/*50 nn*/

    private String lastName;/*50 n*/

    private String fullname;

    private LocalDate birthDate;/*nn*/

    private String gender;/*10 nn*/

    private String phone;/*20 n*/

    private String address;/*500 n*/

    private LocalDate membershipExpireDate;/* nn*/

    public CustomerIndexDto(String membershipNumber, String firstName, String lastName, LocalDate birthDate, String gender, String phone, String address, LocalDate membershipExpireDate) {
        this.membershipNumber = membershipNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.membershipExpireDate = membershipExpireDate;
        this.fullname = firstName+" "+lastName;
    }
}
