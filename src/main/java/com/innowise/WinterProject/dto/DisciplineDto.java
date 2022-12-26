package com.innowise.WinterProject.dto;

import com.innowise.WinterProject.group.Creation;
import com.innowise.WinterProject.group.Update;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisciplineDto {
    @NotNull(groups = Update.class)
    private UUID id;

    @NotEmpty(groups = Creation.class)
    @Size(max = 50, groups = {Creation.class, Update.class})
    private String name;
}
