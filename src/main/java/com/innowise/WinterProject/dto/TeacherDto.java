package com.innowise.WinterProject.dto;

import com.innowise.WinterProject.group.Creation;
import com.innowise.WinterProject.group.Update;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class TeacherDto { //extends  UserDto

    @NotNull(groups = {Update.class,Creation.class})
    private UUID id;

    @NotEmpty(groups = Creation.class)
    @Size(max = 30, groups = {Creation.class, Update.class})
    private String firstName;
    @NotEmpty(groups = Creation.class)
    @Size(max = 30, groups = {Creation.class, Update.class})
    private String lastName;
}
