package com.example.winterhold.validation;

import com.example.winterhold.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class CategoryNameValidator implements ConstraintValidator<CategoryName,String> {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        Long result = categoryRepository.getCountCategory(s);
        if(result>0){
        return false;
        }
        return true;
    }
}
