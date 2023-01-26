package com.innowise.WinterProject.service;

import com.innowise.WinterProject.entity.Group;
import com.innowise.WinterProject.exeption.ItemNotFoundException;
import com.innowise.WinterProject.mapper.GroupMapper;
import com.innowise.WinterProject.repository.GroupRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GroupServiceTests {

    @Mock
    GroupRepository groupRepository;

    @Mock
    GroupMapper groupMapper;

    @InjectMocks
    GroupService groupService;

    private static final UUID ID = UUID.randomUUID();

    @Test
    public void getAllGroupsTest() {
        Group group1 = Group.builder().id(UUID.randomUUID()).number(1).numberOfStudents(1).year(1).build();
        Group group2 = Group.builder().id(UUID.randomUUID()).number(2).numberOfStudents(2).year(2).build();
        Group group3 = Group.builder().id(UUID.randomUUID()).number(3).numberOfStudents(3).year(3).build();
        List<Group> groups = Arrays.asList(group1, group2, group3);
        when(groupRepository.findAll()).thenReturn(groups);
        assertEquals(groups, groupService.getAllGroups());

    }

    @Test
    public void getGroupByIdReturnGroupWithSuchId() {
        Group group = Group.builder().id(ID).number(1).numberOfStudents(1).year(1).build();
        when(groupRepository.findById(ID)).thenReturn(Optional.of(group));
        assertEquals(group, groupService.getGroupById(ID));
    }

    @Test
    public void getGroupByIdReturnExceptionOnWrongId() {
        when(groupRepository.findById(ID)).thenReturn(Optional.empty());
        assertThrows(ItemNotFoundException.class, () -> groupService.getGroupById(ID));
    }

    @Test
    public void addGroupReturnAddedGroup() {
        Group group = Group.builder().id(UUID.randomUUID()).number(1).numberOfStudents(1).year(1).build();
        when(groupRepository.save(group)).thenReturn(group);
        assertEquals(group, groupService.addGroup(group));
    }

    @Test
    public void removeGroupTest() {
        groupService.removeGroup(ID);
        verify(groupRepository, times(1)).deleteById(ID);
    }

    @Test
    public void updateGroupReturnUpdatedGroup() {
        Group groupChanges = Group.builder().id(ID).year(2).build();
        Group group = Group.builder().id(ID).number(1).numberOfStudents(1).year(1).build();
        Group groupAfterChanges = Group.builder().id(ID).number(1).numberOfStudents(1).year(2).build();

        when(groupRepository.findById(groupChanges.getId())).thenReturn(Optional.of(group));
        when(groupMapper.updateGroup(groupChanges, group)).thenReturn(groupAfterChanges);
        when(groupRepository.save(groupAfterChanges)).thenReturn(groupAfterChanges);

        assertEquals(groupAfterChanges, groupService.updateGroup(groupChanges));
    }

    @Test
    public void increaseNumberOfStudentsInGroup() {
        Group group = Group.builder().id(ID).number(1).numberOfStudents(1).year(1).build();
        int initalNumberOfStudents = group.getNumberOfStudents();
        when(groupRepository.save(group)).thenReturn(group);

        groupService.increaseNumberOfStudentsInGroup(group);
        int finalNumberOfStudents = group.getNumberOfStudents();
        assertEquals(initalNumberOfStudents + 1, finalNumberOfStudents);
    }

    @Test
    public void decreaseNumberOfStudentsInGroup() {
        Group group = Group.builder().id(ID).number(1).numberOfStudents(1).year(1).build();
        int initalNumberOfStudents = group.getNumberOfStudents();
        when(groupRepository.save(group)).thenReturn(group);

        groupService.decreaseNumberOfStudentsInGroup(group);
        int finalNumberOfStudents = group.getNumberOfStudents();
        assertEquals(initalNumberOfStudents - 1, finalNumberOfStudents);
    }

}
