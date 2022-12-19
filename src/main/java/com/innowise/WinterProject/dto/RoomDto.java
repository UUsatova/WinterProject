package com.innowise.WinterProject.dto;

import com.innowise.WinterProject.group.Creation;
import com.innowise.WinterProject.group.Update;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {

    @NotNull(groups = Update.class)
    private UUID id;

    @Positive( groups = Creation.class)
    private int number;

    @NotEmpty(groups = Creation.class)
    @Size(max = 100, groups = Creation.class)
    @Null(groups = Update.class)
    private String address;
}
