package com.innowise.WinterProject.validationAnnotation;

import com.innowise.WinterProject.validation.IdExistValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.annotation.*;
import java.util.UUID;

@Documented
@Constraint(validatedBy = IdExistValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistInDatabase {

    Class<? extends JpaRepository<?, UUID>> repository();

    String message() default "Id doesn't exist.Please create user before updating";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
