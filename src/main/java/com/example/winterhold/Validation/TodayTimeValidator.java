package com.example.winterhold.Validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class TodayTimeValidator implements ConstraintValidator<TodayTime, LocalDate> {


    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        if(date != null) {
            if (date.isAfter(LocalDate.now())) {
                return false;
            }
        }
        return true;
    }
}
