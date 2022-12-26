package com.innowise.WinterProject.dto;

import com.innowise.WinterProject.entity.Role;
import com.innowise.WinterProject.group.Creation;
import com.innowise.WinterProject.group.Update;
import com.innowise.WinterProject.validation.annotation.UniqueLogin;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class UserDto {

    @Hidden
    @NotNull(groups = Update.class)
    private UUID id;

    @NotEmpty(groups = Creation.class)
    @UniqueLogin(groups = {Creation.class, Update.class})
    @Size(max = 30, groups = {Creation.class, Update.class})
    private String login;

    @NotEmpty(groups = Creation.class)
    @Size(max = 30, groups = {Creation.class, Update.class})
    private String password;

    @NotNull(groups = Creation.class)
    private Role role;

}
