package com.innowise.WinterProject.validation;

import com.innowise.WinterProject.repository.GroupRepository;
import com.innowise.WinterProject.validation.annotation.EmptyGroup;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class DeleteGroupValidator implements
        ConstraintValidator<EmptyGroup, UUID> {
    private GroupRepository groupRepository;

    @Override
    public boolean isValid(UUID id, ConstraintValidatorContext context) {
          return groupRepository.existsByIdAndNumberOfStudents(id,0);
    }

}
