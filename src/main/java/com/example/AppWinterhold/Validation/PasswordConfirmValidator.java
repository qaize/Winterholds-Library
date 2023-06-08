package com.example.AppWinterhold.Validation;

import com.example.AppWinterhold.Service.abs.AccountService;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordConfirmValidator implements ConstraintValidator<PasswordConfirm,Object> {
    private String mainPassword;
    private String confirmPassword;

    @Autowired
    private AccountService accountService;

    @Override
    public void initialize(PasswordConfirm constraintAnnotation){
        this.mainPassword = constraintAnnotation.mainPassword();
        this.confirmPassword = constraintAnnotation.confirmPassword();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {


        String password = new BeanWrapperImpl(o).getPropertyValue(mainPassword).toString();
        String conPassword = new BeanWrapperImpl(o).getPropertyValue(confirmPassword).toString();


        return accountService.passwordChecker(password,conPassword);
    }
}
