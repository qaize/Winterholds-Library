package com.example.winterhold.validation;

import com.example.winterhold.dao.BookRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;


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
