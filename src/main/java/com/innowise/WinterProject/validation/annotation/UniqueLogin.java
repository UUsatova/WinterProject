package com.innowise.WinterProject.validation.annotation;

import com.innowise.WinterProject.group.Creation;
import com.innowise.WinterProject.validation.LoginExistInDataBase;
import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LoginExistInDataBase.class)
@Target( {ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueLogin {
    Class<Creation> groups();
}
