package com.example.winterhold.dto.account;
import com.example.winterhold.validation.PasswordConfirm;
import lombok.*;
import jakarta.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@PasswordConfirm(mainPassword = "password",confirmPassword = "conpassword",message = "Password not Match!")
public class AccountUpdateDto {


    @NotBlank(message = "Username cannot blank")
    @NotNull(message = "Username Cannot Null")
    @Size(max = 20,message = "Max 20 char")
    private String username;

    @NotBlank(message = "Password cannot blank")
    @NotNull(message = "Password Cannot Null")
    @Size(max = 200,message = "Max 200 char")
    private String password;

    @NotBlank(message = "Please Confirm your password")
    @Size(max = 200,message = "Max 200 char")
    private String conpassword;

    public AccountUpdateDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
