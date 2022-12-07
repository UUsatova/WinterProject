package com.innowise.WinterProject.validation;

import com.innowise.WinterProject.validationAnnotation.ExistInDatabase;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

@RequiredArgsConstructor
public class IdExistValidator implements
        ConstraintValidator<ExistInDatabase, UUID> {


    private ApplicationContext applicationContext;
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
