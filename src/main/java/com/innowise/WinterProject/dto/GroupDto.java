package com.innowise.WinterProject.dto;

import com.innowise.WinterProject.group.Creation;
import com.innowise.WinterProject.group.Update;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class GroupDto {

    @NotNull(groups = Update.class)
    private UUID id;


    @Min(value = 1,groups = {Creation.class, Update.class})
    private int number;

    @Min(value = 1,groups = {Creation.class, Update.class}) //надо запретить апдейтить,хотя я же нигде не ставила запрет
    private int numberOfStudents;     //на апдейт айдишника хотя его нельзя апдейтить

    @Min(value = 1,groups = {Creation.class, Update.class})
    private int year;

}
