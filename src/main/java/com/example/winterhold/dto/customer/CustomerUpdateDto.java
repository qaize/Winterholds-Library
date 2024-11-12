package com.example.winterhold.dto.customer;

//import com.example.AppWinterhold.Validation.CustomerName;

import com.example.winterhold.validation.TodayTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerUpdateDto {

    @NotNull(message = "Cannot be Null")
    private String membershipNumber; /*20 nn*/

    @NotBlank(message = "First Name cannot Blank")
    @NotNull(message = "First Name cannot null")
    @Size(max = 50, message = "max 50 char")
    private String firstName;/*50 nn*/


    @Size(max = 50, message = "max 50 char")
    private String lastName;/*50 n*/

    @TodayTime(message = "Cannot insert date more than today!")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Please Insert your birth date")
    private LocalDate birthDate;/*nn*/

    @NotBlank(message = "Chose gender properly")
    @NotNull(message = "Gender cannot null")
    @Size(max = 10, message = "max 10 char")
    private String gender;/*10 nn*/

    @Size(max = 20, message = "max 20 char")
    private String phone;/*20 n*/

    @Size(max = 500, message = "max 500 char")
    private String address;/*500 n*/

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Membership Expire date cannot null!")
    private LocalDate membershipExpireDate;/* nn*/
}
