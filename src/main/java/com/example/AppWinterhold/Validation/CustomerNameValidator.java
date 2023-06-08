package com.example.AppWinterhold.Validation;

import com.example.AppWinterhold.Dao.CategoryRepository;
import com.example.AppWinterhold.Dao.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CustomerNameValidator implements ConstraintValidator<CustomerName,String> {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        Long result = customerRepository.getCountCustomer(s);
        if(result>0){
            return false;
        }
        return true;
    }
}
