package com.innowise.WinterProject.dto;

import com.innowise.WinterProject.group.Creation;
import com.innowise.WinterProject.group.Update;
import com.innowise.WinterProject.repository.StudentRepository;
import com.innowise.WinterProject.validationAnnotation.ExistInDatabase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDto {

    @Valid
    @ExistInDatabase(repository = StudentRepository.class, groups = Update.class)
    private UUID id;

    @Valid
    @NotNull(groups = Creation.class)
    private UUID groupId;

    @Valid
    @NotNull(groups = Creation.class)
    private UUID roomId;

    @Valid
    @NotNull(groups = Creation.class)
    private UUID teacherId;

    @Valid
    @NotNull(groups = Creation.class)
    private UUID disciplineId;

    @Valid
    @NotNull(groups = Creation.class)
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @Valid
    @NotNull(groups = Creation.class)
    @DateTimeFormat(iso=DateTimeFormat.ISO.TIME)
    private LocalTime startTime;

    @Valid
    @NotNull(groups = Creation.class)
    @DateTimeFormat(iso=DateTimeFormat.ISO.TIME)
    private LocalTime endTime;


}
