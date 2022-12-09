package com.innowise.WinterProject.validation;

import com.innowise.WinterProject.entity.Student;
import com.innowise.WinterProject.repository.GroupRepository;
import com.innowise.WinterProject.repository.StudentRepository;
import com.innowise.WinterProject.validationAnnotation.EmptyGroup;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;

import java.util.UUID;

@RequiredArgsConstructor
public class DeleteGroupValidator implements
        ConstraintValidator<EmptyGroup, UUID> {
    private StudentRepository studentRepository;
    private GroupRepository groupRepository;

    @Override
    public boolean isValid(UUID id, ConstraintValidatorContext context) {
        Example<Student> example = Example.of(new Student(null, groupRepository.findById(id).get(), null, null));
        return studentRepository.exists(example);
    } //тут что-то страшное,сыглядит слишком мудрено поэтому я не уверенна
}
