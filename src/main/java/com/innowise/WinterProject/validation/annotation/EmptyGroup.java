package com.innowise.WinterProject.validation.annotation;

import com.innowise.WinterProject.validation.DeleteGroupValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DeleteGroupValidator.class)
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmptyGroup {

    String message() default "Group is not empty,you can't delete it.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
