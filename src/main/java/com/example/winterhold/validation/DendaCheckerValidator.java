package com.example.winterhold.validation;

import com.example.winterhold.dao.LoanRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;


public class DendaCheckerValidator implements ConstraintValidator<DendaChecker,String> {

    @Autowired
    private LoanRepository loanRepository;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        Long result = loanRepository.findCostumerOnLoan(s);
        if(result>0){
            return false;
        }
        return true;
    }

}
