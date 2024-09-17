package com.example.winterhold.validation;

import com.example.winterhold.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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
