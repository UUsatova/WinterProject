package com.innowise.WinterProject.controller;

import com.innowise.WinterProject.dto.ScheduleDto;
import com.innowise.WinterProject.group.Creation;
import com.innowise.WinterProject.group.Update;
import com.innowise.WinterProject.mapper.ScheduleMapper;
import com.innowise.WinterProject.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final ScheduleMapper scheduleMapper;

    @GetMapping
    public ResponseEntity<List<ScheduleDto>> getSchedule() {
        return ResponseEntity.ok(scheduleService.getAllSchedule()
                .stream().map(scheduleMapper::scheduleToDto)
                .toList());
    }


    @GetMapping(value = "/{id}") //сделать норм возврат
    public ResponseEntity<ScheduleDto> getScheduleById(@PathVariable UUID id) {
        return ResponseEntity.ok(scheduleMapper.scheduleToDto(scheduleService.getScheduleById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ScheduleDto> addSchedule(@RequestBody @Validated( Creation.class) ScheduleDto scheduleDto) {
        return ResponseEntity.ok(scheduleMapper.scheduleToDto(
                scheduleService.addSchedule(scheduleDto)));
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> removeSchedule(@PathVariable UUID id) {
        scheduleService.removeSchedule(id);
        return ResponseEntity.ok().build();
    }

    //только администратор
    //почему-то проваливается валидация без понятия почему
    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ScheduleDto> updateSchedule(@RequestBody @Validated(Update.class) ScheduleDto scheduleDto) { //@Validated(Update.class)
        return ResponseEntity.ok(scheduleMapper.scheduleToDto(
                scheduleService.updateSchedule(scheduleMapper.dtoToSchedule(scheduleDto))));
    } //+сделать норм апдейт

    @DeleteMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> removeAllSchedule() {
        scheduleService.removeAll();
        return ResponseEntity.ok().build();
    }
}


/*
student  сделать нормальный вывод дто в апдейте и креате
            кастомизировать аптейт (дать разрешение что-тот менять студенту)
teacher кастомизировать аптейт (дать разрешение что-тот менять)
schedule починиить апдейт
         сделатьб нормальный вывод дто в апдейте и креате
Сделатьтт проверку на уникальность логина,а то ошибка
 */