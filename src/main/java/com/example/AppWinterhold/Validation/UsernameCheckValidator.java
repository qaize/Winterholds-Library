package com.example.AppWinterhold.Validation;

import com.example.AppWinterhold.Dao.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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
