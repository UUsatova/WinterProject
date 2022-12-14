package com.innowise.WinterProject.controller;

import com.innowise.WinterProject.dto.DisciplineDto;
import com.innowise.WinterProject.group.Creation;
import com.innowise.WinterProject.group.Update;
import com.innowise.WinterProject.mapper.DisciplineMapper;
import com.innowise.WinterProject.service.DisciplineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value ="/disciplines")
public class DisciplineController {
    private final DisciplineService disciplineService;
    private final DisciplineMapper disciplineMapper;


    @GetMapping
    public ResponseEntity<List<DisciplineDto>> getDisciplines() {
        return ResponseEntity.ok(disciplineService.getAllDisciplines()
                .stream().map(disciplineMapper::disciplineToDto)
                .toList());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DisciplineDto> getDisciplineById(@PathVariable UUID id) {
        return ResponseEntity.ok(disciplineMapper.disciplineToDto(disciplineService.getDisciplineById(id)));
    }

    @PostMapping
    public ResponseEntity<DisciplineDto> addDiscipline(@RequestBody @Validated(Creation.class) DisciplineDto disciplineDto) {
        return ResponseEntity.ok(disciplineMapper.disciplineToDto(
                disciplineService.addDiscipline(disciplineMapper.dtoToDiscipline(disciplineDto))));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> removeDiscipline(@PathVariable UUID id) {
        disciplineService.removeDiscipline(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<DisciplineDto> updateDiscipline(@RequestBody @Validated(Update.class) DisciplineDto disciplineDto) {
        return ResponseEntity.ok(disciplineMapper.disciplineToDto(
                disciplineService.updateDiscipline(disciplineMapper.dtoToDiscipline(disciplineDto))));
    }
}
