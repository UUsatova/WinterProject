package com.innowise.WinterProject.controller;

import com.innowise.WinterProject.dto.DisciplineDto;
import com.innowise.WinterProject.group.Creation;
import com.innowise.WinterProject.group.Update;
import com.innowise.WinterProject.mapper.DisciplineMapper;
import com.innowise.WinterProject.service.DisciplineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value ="/disciplines")
public class DisciplineController {
    private final DisciplineService disciplineService;
    private final DisciplineMapper disciplineMapper;


    @Operation(summary = "Get all disciplines")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",content =
                    { @Content(mediaType = "Application/json",
                            schema = @Schema(implementation = DisciplineDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)})
    @GetMapping
    public ResponseEntity<List<DisciplineDto>> getDisciplines() {
        return ResponseEntity.ok(disciplineService.getAllDisciplines()
                .stream().map(disciplineMapper::disciplineToDto)
                .toList());
    }

    @Operation(summary = "Get discipline by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",content =
                    { @Content(mediaType = "Application/json",
                            schema = @Schema(implementation = DisciplineDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)})
    @GetMapping(value = "/{id}")
    public ResponseEntity<DisciplineDto> getDisciplineById(@Parameter(description = "discipline' id",example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") @PathVariable UUID id) {
        return ResponseEntity.ok(disciplineMapper.disciplineToDto(disciplineService.getDisciplineById(id)));
    }


    @Operation(summary = "Add discipline")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",content =
                    { @Content(mediaType = "Application/json",
                            schema = @Schema(implementation = DisciplineDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)})
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DisciplineDto> addDiscipline(@RequestBody @Validated(Creation.class) DisciplineDto disciplineDto) {
        return ResponseEntity.ok(disciplineMapper.disciplineToDto(
                disciplineService.addDiscipline(disciplineMapper.dtoToDiscipline(disciplineDto))));
    }

    @Operation(summary = "Delete discipline")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeDiscipline(@PathVariable UUID id) {
        disciplineService.removeDiscipline(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update discipline")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",content =
                    { @Content(mediaType = "Application/json",
                            schema = @Schema(implementation = DisciplineDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)})
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DisciplineDto> updateDiscipline(@RequestBody @Validated(Update.class) DisciplineDto disciplineDto) {
        return ResponseEntity.ok(disciplineMapper.disciplineToDto(
                disciplineService.updateDiscipline(disciplineMapper.dtoToDiscipline(disciplineDto))));
    }
}
