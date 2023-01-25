package com.innowise.WinterProject.controller;

import com.innowise.WinterProject.dto.GroupDto;
import com.innowise.WinterProject.group.Creation;
import com.innowise.WinterProject.group.Update;
import com.innowise.WinterProject.mapper.GroupMapper;
import com.innowise.WinterProject.repository.GroupRepository;
import com.innowise.WinterProject.service.GroupService;
import com.innowise.WinterProject.validation.annotation.EmptyGroup;
import com.innowise.WinterProject.validation.annotation.ExistInDatabase;
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
@RequestMapping(value = "/groups")
public class GroupController {

    private final GroupService groupService;
    private final GroupMapper groupMapper;

    @Operation(summary = "Get all groups")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",content =
                    { @Content(mediaType = "Application/json",
                            schema = @Schema(implementation = GroupDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)})
    @GetMapping
    public ResponseEntity<List<GroupDto>> getGroups() {
        return ResponseEntity.ok(groupService.getAllGroups()
                .stream().map(groupMapper::groupToDto)
                .toList());
    }

    @Operation(summary = "Get group by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",content =
                    { @Content(mediaType = "Application/json",
                            schema = @Schema(implementation = GroupDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)})
    @GetMapping(value = "/{id}")
    public ResponseEntity<GroupDto> getGroupById(@Parameter(description = "groups' id",example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") @PathVariable UUID id) {
        return ResponseEntity.ok(groupMapper.groupToDto(groupService.getGroupById(id)));
    }

    @Operation(summary = "Add group ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",content =
                    { @Content(mediaType = "Application/json",
                            schema = @Schema(implementation = GroupDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)})
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GroupDto> addGroup(@RequestBody @Validated(Creation.class) GroupDto newGroup) {
        return ResponseEntity.ok(groupMapper.groupToDto(
                groupService.addGroup(groupMapper.dtoToGroup(newGroup))));
    }

    @Operation(summary = "Delete group ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeGroup(@PathVariable @ExistInDatabase(repository = GroupRepository.class) @EmptyGroup UUID id) {
        groupService.removeGroup(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update group ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",content =
                    { @Content(mediaType = "Application/json",
                            schema = @Schema(implementation = GroupDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)})
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GroupDto> updateGroup(@RequestBody @Validated(Update.class) GroupDto groupDto) {
        return ResponseEntity.ok(groupMapper.groupToDto(
                groupService.updateGroup(groupMapper.dtoToGroup(groupDto))));
    }
}
