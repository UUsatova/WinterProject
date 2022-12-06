package com.innowise.WinterProject.dto;

import com.innowise.WinterProject.group.Set;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data /////////////////
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {


    private UUID id;

    @NotNull(groups = Set.class)
    private UUID groupId;
    @NotEmpty
    @Size(max = 30)
    private String firstName;

    @NotEmpty
    @Size(max = 30)
    private String lastName;

}
