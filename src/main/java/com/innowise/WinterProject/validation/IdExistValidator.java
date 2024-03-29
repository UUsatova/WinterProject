package com.innowise.WinterProject.validation;

import com.innowise.WinterProject.validation.annotation.ExistInDatabase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

@RequiredArgsConstructor
public class IdExistValidator implements
        ConstraintValidator<ExistInDatabase, UUID> {


    private final ApplicationContext applicationContext;
    private JpaRepository<Object, UUID> repository;

    @Override
    public void initialize(ExistInDatabase constraintAnnotation) {
        repository = (JpaRepository) applicationContext.getBean(constraintAnnotation.repository());
    }

    @Override
    public boolean isValid(UUID id, ConstraintValidatorContext context) {
        return repository.existsById(id);
    }
}
