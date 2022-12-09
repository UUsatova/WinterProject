package com.innowise.WinterProject.validation;

import com.innowise.WinterProject.servise.StudentService;
import com.innowise.WinterProject.validationAnnotation.EmptyGroup;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

//Вроде бы ничего аналогичного нет чтобы обобщить
@RequiredArgsConstructor
public class DeleteGroupValidator implements
        ConstraintValidator<EmptyGroup, UUID> {
    private StudentService studentService;

    @Override
    public boolean isValid(UUID id, ConstraintValidatorContext context) {
        return studentService.getAllStudents().stream()
                .noneMatch(student -> student.getGroup().getId().equals(id));
    }
}
