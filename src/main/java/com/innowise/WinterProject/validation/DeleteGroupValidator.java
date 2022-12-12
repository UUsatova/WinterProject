package com.innowise.WinterProject.validation;

import com.innowise.WinterProject.repository.GroupRepository;
import com.innowise.WinterProject.validationAnnotation.EmptyGroup;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class DeleteGroupValidator implements
        ConstraintValidator<EmptyGroup, UUID> {
    private GroupRepository groupRepository;

//    @Override
//    public boolean isValid(UUID id, ConstraintValidatorContext context) {
//          return groupRepository.existsByIdAndNumberOfStudents(id,groupRepository.findById(id).get().getNumberOfStudents());
//    }

// метод который мы написали проверяет существование группы с таким айдишником и таким кол. студентов
// а мне надо чтобы в группе с таким айдишником студентов было 0 студентов,тогда можно удалять

    @Override
    public boolean isValid(UUID id, ConstraintValidatorContext context) {
        return groupRepository.findById(id).get().getNumberOfStudents() == 0;
    }


}
