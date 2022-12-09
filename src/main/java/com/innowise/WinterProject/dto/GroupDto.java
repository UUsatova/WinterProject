package com.innowise.WinterProject.dto;

import com.innowise.WinterProject.group.Creation;
import com.innowise.WinterProject.group.Update;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;

import java.util.UUID;

@Data
public class GroupDto {

    @NotNull(groups = Update.class)
    private UUID id;


    @Min(value = 1, groups = Creation.class)
    private int number;

    @Null(groups = {Creation.class, Update.class})
    @Min(value = 1, groups = {Creation.class, Update.class})
    private int numberOfStudents;

    @Min(value = 1, groups = Creation.class)
    private int year;

}
