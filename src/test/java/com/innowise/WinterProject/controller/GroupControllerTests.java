package com.innowise.WinterProject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.innowise.WinterProject.dto.GroupDto;
import com.innowise.WinterProject.entity.Group;
import com.innowise.WinterProject.mapper.GroupMapper;
import com.innowise.WinterProject.security.JwtFilter;
import com.innowise.WinterProject.security.JwtProvider;
import com.innowise.WinterProject.security.SecurityConfig;
import com.innowise.WinterProject.service.GroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {GroupController.class})
@ContextConfiguration(classes = {GroupController.class, SecurityConfig.class, JwtProvider.class, JwtFilter.class})
public class GroupControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GroupService groupService;
    @MockBean
    private GroupMapper groupMapper;
    @Autowired
    ObjectMapper mapper;

    private static final String URL  = "/groups";
    private static final String URL_WITH_ID  = "/groups/123e4567-e89b-12d3-a456-426614174000";

    @Test
    @WithMockUser
    public void getGroupsReturnListOfGroupsInJsonArray() throws Exception {

        Group group1 = Group.builder().id(UUID.randomUUID()).number(1).numberOfStudents(1).year(1).build();
        Group group2 = Group.builder().id(UUID.randomUUID()).number(2).numberOfStudents(2).year(2).build();
        Group group3 = Group.builder().id(UUID.randomUUID()).number(3).numberOfStudents(3).year(3).build();
        List<Group> groups = Arrays.asList(group1, group2, group3);
        when(groupService.getAllGroups()).thenReturn(groups);
        GroupDto groupDto1 = new GroupDto(UUID.randomUUID(), 1, 10, 1);
        GroupDto groupDto2 = new GroupDto(UUID.randomUUID(), 2, 20, 2);
        GroupDto groupDto3 = new GroupDto(UUID.randomUUID(), 3, 30, 3);

        when(groupMapper.groupToDto(group1)).thenReturn(groupDto1);
        when(groupMapper.groupToDto(group2)).thenReturn(groupDto2);
        when(groupMapper.groupToDto(group3)).thenReturn(groupDto3);


        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].number", equalTo(group1.getNumber())))
                .andExpect(jsonPath("[0].numberOfStudents", equalTo(group1.getNumberOfStudents())))
                .andExpect(jsonPath("[0].year", equalTo(group1.getYear())))
                .andExpect(jsonPath("[1].number", equalTo(group2.getNumber())))
                .andExpect(jsonPath("[1].numberOfStudents", equalTo(group2.getNumberOfStudents())))
                .andExpect(jsonPath("[1].year", equalTo(group2.getYear())))
                .andExpect(jsonPath("[2].number", equalTo(group3.getNumber())))
                .andExpect(jsonPath("[2].numberOfStudents", equalTo(group3.getNumberOfStudents())))
                .andExpect(jsonPath("[2].year", equalTo(group3.getYear())));
    }

    @Test
    @WithAnonymousUser
    public void getGroupsExecuteWithUnknownUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser
    public void getGroupByIdReturnUserByPassedId() throws Exception {
        Group group = Group.builder().id(UUID.randomUUID()).number(1).numberOfStudents(1).year(1).build();

        when(groupService.getGroupById(any(UUID.class))).thenReturn(group);
        when(groupMapper.groupToDto(group)).thenReturn(new GroupDto(UUID.randomUUID(), 1, 10, 1));

        mockMvc.perform(MockMvcRequestBuilders.get(URL_WITH_ID)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number", equalTo(group.getNumber())))
                .andExpect(jsonPath("$.numberOfStudents", equalTo(group.getNumberOfStudents())))
                .andExpect(jsonPath("$.year", equalTo(group.getYear())));
    }

    @Test
    @WithAnonymousUser
    public void getGroupByIdExecuteWithUnknownUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_WITH_ID)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void addGroupReturnAddedGroup() throws Exception {
        GroupDto groupDto = new GroupDto(null, 1, null, 1);
        Group group = Group.builder().id(UUID.randomUUID()).number(1).numberOfStudents(1).year(1).build();
        GroupDto expectedGroupDto = new GroupDto(UUID.randomUUID(), 1, 1, 1);

        when(groupMapper.dtoToGroup(groupDto)).thenReturn(group);
        when(groupService.addGroup(group)).thenReturn(group);
        when(groupMapper.groupToDto(group)).thenReturn(expectedGroupDto);

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .content(new Gson().toJson(groupDto)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number", equalTo(group.getNumber())))
                .andExpect(jsonPath("$.numberOfStudents", equalTo(group.getNumberOfStudents())))
                .andExpect(jsonPath("$.year", equalTo(group.getYear())));

    }

    @Test
    @WithMockUser(roles = {"TEACHER", "STUDENT"})
    public void addGroupExecutedWithoutAdminRightsReturn403() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .content("{\"number\":1,\"year\":1}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void removeGroupReturn202AndExecuteGroupService() throws Exception {
        doNothing().when(groupService).removeGroup(any(UUID.class));
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_WITH_ID)
                .contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        verify(groupService, times(1)).removeGroup(any(UUID.class));
    }

    @Test
    @WithMockUser(roles = {"TEACHER", "STUDENT"})
    public void removeGroupExecutedWithoutAdminRightsReturn403() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_WITH_ID)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
        verify(groupService, times(0)).removeGroup(any(UUID.class));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateGroupReturnAddedGroup() throws Exception {

        GroupDto groupDto = new GroupDto(UUID.randomUUID(), 1, null, 1);
        Group group = Group.builder().id(UUID.randomUUID()).number(1).numberOfStudents(1).year(1).build();
        GroupDto expectedGroupDto = new GroupDto(UUID.randomUUID(), 1, 1, 1);

        when(groupMapper.dtoToGroup(groupDto)).thenReturn(group);
        when(groupService.updateGroup(group)).thenReturn(group);
        when(groupMapper.groupToDto(group)).thenReturn(expectedGroupDto);

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                .content(new Gson().toJson(groupDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.number", equalTo(group.getNumber())))
                .andExpect(jsonPath("$.numberOfStudents", equalTo(group.getNumberOfStudents())))
                .andExpect(jsonPath("$.year", equalTo(group.getYear())));

    }

    @Test
    @WithMockUser(roles = {"TEACHER", "STUDENT"})
    public void updateGroupExecutedWithoutAdminRightsReturn403() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                .content("{\"id\":\"123e4567-e89b-12d3-a456-426614174000\",\"number\":1,\"year\":1}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());

    }


}
