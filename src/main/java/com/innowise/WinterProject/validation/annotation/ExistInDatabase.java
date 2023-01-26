package com.innowise.WinterProject.validation.annotation;

import com.innowise.WinterProject.validation.IdExistValidator;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.UUID;

@Documented
@Constraint(validatedBy = IdExistValidator.class)
@Target( {ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistInDatabase {

    Class<? extends JpaRepository<?, UUID>> repository();

    String message() default "Id doesn't exist.Please create before updating";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
