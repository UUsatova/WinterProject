package com.innowise.WinterProject.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.innowise.WinterProject.group.Creation;
import com.innowise.WinterProject.group.Update;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
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
