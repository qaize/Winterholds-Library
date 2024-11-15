package com.example.winterhold.dto.account;
import com.example.winterhold.validation.PasswordConfirm;
import com.example.winterhold.validation.UsernameCheck;
import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@PasswordConfirm(mainPassword = "password",confirmPassword = "conpassword",message = "Password not Match!")
public class AccountInsertDto {

    @NotNull(message = "You should insert some value")
    @NotBlank(message = "Fill this form")
    private String name;

    @UsernameCheck(message = "This Username Already used!")
    @NotBlank(message = "Username cannot blank")
    @NotNull(message = "Username Cannot Null")
    @Size(max = 200,message = "Max 20 char")
    private String username;

    @NotBlank(message = "Password cannot blank")
    @NotNull(message = "Password Cannot Null")
    @Size(max = 200,message = "Max 200 char")
    private String password;

    @NotBlank(message = "Please Confirm your password")
    @Size(max = 200,message = "Max 200 char")
    private String conpassword;

    @Size(max = 200,message = "Max 200 char")
    private String email;


    private String role;

    public AccountInsertDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
