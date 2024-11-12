package com.example.winterhold.validation;

import com.example.winterhold.repository.CategoryRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;



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
