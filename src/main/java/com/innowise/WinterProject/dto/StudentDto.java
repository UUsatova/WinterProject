package com.innowise.WinterProject.dto;

import com.innowise.WinterProject.validationAnnotation.ExistInDatabase;
import com.innowise.WinterProject.group.Creation;
import com.innowise.WinterProject.group.Update;
import com.innowise.WinterProject.repository.StudentRepository;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data 
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {

    @ExistInDatabase(repository = StudentRepository.class, groups =  Update.class)
    private UUID id;

    @NotNull(groups = Creation.class)
    private UUID groupId;
    @NotEmpty(groups = {Creation.class, Update.class})
    @Size(max = 30,groups = {Creation.class, Update.class})
    private String firstName;

    @NotEmpty(groups = {Creation.class, Update.class})
    @Size(max = 30,groups = {Creation.class, Update.class})
    private String lastName;

}
