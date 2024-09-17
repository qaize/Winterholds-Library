package com.example.winterhold.validation;

import com.example.winterhold.dao.AccountRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;


public class UsernameCheckValidator implements ConstraintValidator<UsernameCheck,String> {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        Long data = accountRepository.getCountUsernameByUsername(s);

        if(data>0){
            return false;
        }
        return true;
    }
}
