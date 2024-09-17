package com.example.winterhold.validation;

import com.example.winterhold.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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
