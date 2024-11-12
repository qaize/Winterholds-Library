package com.example.winterhold.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = BookPlacingValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BookPlacing {
    public Class <?>[] groups() default {};
    public Class <? extends Payload>[] payload() default {};

    public String message();

    public String isle();
    public String bay();
    public String floor();
}
