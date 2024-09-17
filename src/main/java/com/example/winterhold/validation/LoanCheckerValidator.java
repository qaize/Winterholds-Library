package com.example.winterhold.validation;

import com.example.winterhold.dao.CustomerRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;


public class LoanCheckerValidator implements ConstraintValidator<LoanChecker, String> {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {


        if (!s.isBlank()) {
            Integer result = customerRepository.getLoanCountCurrentCustomer(s);
            if (result >= 3) {
                return false;
            }
        }
        return true;
    }
}
