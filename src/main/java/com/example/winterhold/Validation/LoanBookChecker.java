package com.example.winterhold.Validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = LoanBookCheckerValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoanBookChecker {

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};

    public String message();

    public String customer();

    public String book();
}
