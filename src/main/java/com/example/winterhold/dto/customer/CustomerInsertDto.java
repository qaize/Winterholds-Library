package com.example.winterhold.dto.customer;
//import com.example.AppWinterhold.Validation.CustomerName;
import com.example.winterhold.validation.TodayTime;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class CustomerInsertDto {
//    @CustomerName(message = "This Customer ID Already Exist")
//    @NotBlank(message = "Membership Number cannot Blank")
//    @NotNull(message = "Cannot be Null")
//    private String membershipNumber; /*20 nn*/

    private  String membershipNumber;

    @NotBlank(message = "First Name cannot Blank")
    @NotNull(message = "First Name cannot null")
    @Size(max = 50,message = "max 50 char")
    private String firstName;/*50 nn*/


    @Size(max = 50,message = "max 50 char")
    private String lastName;/*50 n*/

    @TodayTime(message = "Cannot insert date more than today!")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Please Insert your birth date")
    private LocalDate birthDate;/*nn*/

    @NotBlank(message = "Chose gender properly")
    @NotNull(message = "Gender cannot null")
    @Size(max = 10,message = "max 10 char")
    private String gender;/*10 nn*/

    @Size(max = 20,message = "max 20 char")
    private String phone;/*20 n*/

    @Size(max = 500,message = "max 500 char")
    private String address;/*500 n*/

    public CustomerInsertDto(String firstName, String lastName, LocalDate birthDate, String gender, String phone, String address, LocalDate membershipExpireDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.membershipExpireDate = membershipExpireDate;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Membership Expire date cannot null!")
    private LocalDate membershipExpireDate;/* nn*/


    public  CustomerInsertDto(String membershipNumber, String firstName,LocalDate membershipExpireDate){
        this.membershipNumber =membershipNumber;
        this.firstName = firstName;
        this.membershipExpireDate = membershipExpireDate;
    }
}
