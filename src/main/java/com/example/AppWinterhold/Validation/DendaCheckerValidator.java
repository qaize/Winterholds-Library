package com.example.AppWinterhold.Validation;

import com.example.AppWinterhold.Dao.BookRepository;
import com.example.AppWinterhold.Dao.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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
