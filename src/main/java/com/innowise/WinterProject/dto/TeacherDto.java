package com.innowise.WinterProject.dto;

import com.innowise.WinterProject.group.Creation;
import com.innowise.WinterProject.group.Update;
import jakarta.validation.Valid;
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
public class TeacherDto {

    @NotNull(groups = Update.class)
    private UUID id;

    @NotEmpty(groups = Creation.class)
    @Size(max = 30, groups = {Creation.class, Update.class})
    private String firstName;
    @NotEmpty(groups = Creation.class)
    @Size(max = 30, groups = {Creation.class, Update.class})
    private String lastName;

    @Valid
    @NotNull(groups = Creation.class)
    private UserDto userDto;
}
