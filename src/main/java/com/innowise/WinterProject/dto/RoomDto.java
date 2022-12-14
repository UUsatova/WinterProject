package com.innowise.WinterProject.dto;

import com.innowise.WinterProject.group.Creation;
import com.innowise.WinterProject.group.Update;
import jakarta.validation.constraints.*;

import java.util.UUID;

public class RoomDto {

    @NotNull(groups = Update.class)
    private UUID id;

    @Min(value = 1, groups = Creation.class)
    private int number;

    @NotEmpty(groups = Creation.class)
    @Size(max = 100, groups = Creation.class)
    @Null(groups = Update.class)
    private String address;
}
