package com.example.winterhold.validation;

import com.example.winterhold.dao.CustomerRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerNameValidator implements ConstraintValidator<CustomerName,String> {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        Long result = customerRepository.checkCustomerById(s);
        if(result>0){
            return false;
        }
        return true;
    }
}
