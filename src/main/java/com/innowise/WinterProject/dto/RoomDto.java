package com.innowise.WinterProject.dto;

import com.innowise.WinterProject.group.Creation;
import com.innowise.WinterProject.group.Update;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class RoomDto {

    @NotNull(groups = Update.class)
    private UUID id;

    @Min(value = 1,groups = {Creation.class, Update.class})
    private int number;

    @NotEmpty(groups = Creation.class)        //было бы логично запретить менять это поле,так как другой адресс
    @Size(max = 100,groups = Creation.class)  //это уже по сути другая комната
    private String address;                    //можно ли и надо ли?
}
