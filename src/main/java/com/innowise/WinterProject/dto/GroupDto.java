package com.innowise.WinterProject.dto;

import com.innowise.WinterProject.group.Creation;
import com.innowise.WinterProject.group.Update;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDto {

    @NotNull(groups = Update.class) //    @ExistInDatabase(repository = StudentRepository.class, groups = Update.class)
    private UUID id;

    @Min(value = 1, groups = Creation.class)
    private int number;

   @Null(groups = {Update.class, Creation.class})
    private Integer numberOfStudents;

    @Min(value = 1, groups = Creation.class)
    private int year;

}
