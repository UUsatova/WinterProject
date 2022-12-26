package com.innowise.WinterProject.dto;

import com.innowise.WinterProject.group.Creation;
import com.innowise.WinterProject.group.Update;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDto {

    @NotNull(groups = Update.class)
    private UUID id;

    @Min(value = 1, groups = Creation.class)
    private int number;

   @Null(groups = {Update.class, Creation.class})
    private Integer numberOfStudents;

    @Min(value = 1, groups = Creation.class)
    private int year;

}
