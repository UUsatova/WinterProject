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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDto {

    @ExistInDatabase(repository = StudentRepository.class, groups = Update.class)
    private UUID id;

    @Valid
    @NotNull(groups = Creation.class)
    private GroupDto groupDto;

    @Valid
    @NotNull(groups = Creation.class)
    private RoomDto roomDto;

    @Valid
    @NotNull(groups = Creation.class)
    private TeacherDto teacherDto;

    @Valid
    @NotNull(groups = Creation.class)
    private DisciplineDto disciplineDto;

    @NotNull(groups = Creation.class)
    private LocalDate date;

    @NotNull(groups = Creation.class)
    private LocalTime startTime;

    @NotNull(groups = Creation.class)
    private LocalTime endTime;


}
