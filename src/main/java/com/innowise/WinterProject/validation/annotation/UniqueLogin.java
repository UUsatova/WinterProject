package com.innowise.WinterProject.validation.annotation;

import com.innowise.WinterProject.validation.LoginExistInDataBase;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LoginExistInDataBase.class)
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueLogin {

    String message() default "Change login please";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
