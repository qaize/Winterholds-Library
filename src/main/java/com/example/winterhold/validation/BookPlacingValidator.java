package com.example.winterhold.validation;

import com.example.winterhold.service.imp.CategoryServiceImp;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;


public class BookPlacingValidator implements ConstraintValidator<BookPlacing,Object> {

    private String floor;
    private String bay;
    private String isle;

    @Autowired
    CategoryServiceImp categoryServiceImp;

    @Override
    public void initialize(BookPlacing constraintAnnotation) {

        this.floor = constraintAnnotation.floor();
        this.bay = constraintAnnotation.bay();
        this.isle = constraintAnnotation.isle();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {

        Integer floors = Integer.parseInt(new BeanWrapperImpl(o).getPropertyValue(floor)==null?"0":new BeanWrapperImpl(o).getPropertyValue(floor).toString());
        String bays = new BeanWrapperImpl(o).getPropertyValue(bay).toString();
        String isles = new BeanWrapperImpl(o).getPropertyValue(isle).toString();

        return !(categoryServiceImp.categoryChecker(isles,floors,bays)>0);
    }
}
