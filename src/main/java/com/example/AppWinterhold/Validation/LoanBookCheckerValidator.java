package com.example.AppWinterhold.Validation;

import com.example.AppWinterhold.Dao.LoanRepository;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LoanBookCheckerValidator implements ConstraintValidator<LoanBookChecker, Object> {

    private String customer;
    private String book;

    @Autowired
    private LoanRepository loanRepository;

    @Override
    public void initialize(LoanBookChecker constraintAnnotation) {

        this.customer = constraintAnnotation.customer();
        this.book = constraintAnnotation.book();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        String customers = new BeanWrapperImpl(o).getPropertyValue(customer).toString();
        String books = new BeanWrapperImpl(o).getPropertyValue(book).toString();

        Long result = loanRepository.validateReplicateBookLoan(customers,books);

        if(result>0){return false;}

        return true;
    }
}
