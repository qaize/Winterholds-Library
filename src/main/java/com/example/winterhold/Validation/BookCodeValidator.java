package com.example.winterhold.Validation;

import com.example.winterhold.Dao.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BookCodeValidator implements ConstraintValidator<BookCode,String> {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        Long result = bookRepository.getCountBooksByCode(s);
        if(result>0){
            return false;
        }
        return true;
    }
}
