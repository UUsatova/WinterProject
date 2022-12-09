package com.innowise.WinterProject.controller;

import com.innowise.WinterProject.dto.GroupDto;
import com.innowise.WinterProject.group.Creation;
import com.innowise.WinterProject.group.Update;
import com.innowise.WinterProject.mapper.GroupMapper;
import com.innowise.WinterProject.servise.GroupService;
import com.innowise.WinterProject.validationAnnotation.EmptyGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/groups")
public class GroupController {

    private final GroupService groupService;
    private final GroupMapper groupMapper;

    @GetMapping
    public ResponseEntity<List<GroupDto>> getGroups() {
        return ResponseEntity.ok(groupService.getAllGroups()
                .stream().map(groupMapper::groupToDto)
                .toList());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<GroupDto> getGroupById(@PathVariable UUID id) {
        return ResponseEntity.ok(groupMapper.groupToDto(groupService.getGroupById(id)));
    }

    @PostMapping
    public ResponseEntity<GroupDto> addGroup(@RequestBody @Validated(Creation.class) GroupDto newGroup) {
        return ResponseEntity.ok(groupMapper.groupToDto(
                groupService.addGroup(groupMapper.dtoToGroup(newGroup))));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> removeGroup(@PathVariable @EmptyGroup UUID id) {
        groupService.removeGroup(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<GroupDto> updateGroup(@RequestBody @Validated(Update.class) GroupDto groupDto) {
        return ResponseEntity.ok(groupMapper.groupToDto(
                groupService.updateGroup(groupMapper.dtoToGroup(groupDto))));
    }
}
