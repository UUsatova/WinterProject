package com.innowise.WinterProject.dto;

import com.innowise.WinterProject.entity.Role;
import com.innowise.WinterProject.group.Creation;
import com.innowise.WinterProject.group.Update;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {

    @NotNull(groups = Update.class) //    @ExistInDatabase(repository = StudentRepository.class, groups = Update.class)
    private UUID id;

    @NotEmpty(groups = Creation.class)
    @Size(max = 30, groups = {Creation.class, Update.class})
    private String login;

    @NotEmpty(groups = Creation.class)
    @Size(max = 30, groups = {Creation.class, Update.class})
    private String password;

    @NotNull(groups = Creation.class)
    private Role role;

}