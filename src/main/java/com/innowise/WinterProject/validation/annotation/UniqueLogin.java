package com.innowise.WinterProject.validation.annotation;

import com.innowise.WinterProject.validation.LoginExistInDataBase;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LoginExistInDataBase.class)
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueLogin {

    String message() default "User with this login already exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
