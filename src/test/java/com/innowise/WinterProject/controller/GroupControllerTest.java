package com.innowise.WinterProject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.WinterProject.dto.GroupDto;
import com.innowise.WinterProject.entity.Group;
import com.innowise.WinterProject.mapper.GroupMapper;
import com.innowise.WinterProject.security.JwtFilter;
import com.innowise.WinterProject.security.JwtProvider;
import com.innowise.WinterProject.security.SecurityConfig;
import com.innowise.WinterProject.service.GroupService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {GroupController.class})
@ContextConfiguration(classes = {GroupController.class, SecurityConfig.class, JwtProvider.class, JwtFilter.class})
public class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GroupService groupService;
    @MockBean
    private GroupMapper groupMapper;
    @Autowired
    ObjectMapper mapper;

    @Nested
    @DisplayName("getGroups")
    class GetGroups {
        @Test
        @WithMockUser
        @DisplayName("GET returns json array with list Of existed groups")
        public void getGroups_returnListOfGroupsInJsonArray() throws Exception {

            Group group1 = new Group(new UUID(1111, 1111), 1, 10, 1);
            Group group2 = new Group(new UUID(2222, 2222), 2, 20, 2);
            Group group3 = new Group(new UUID(3333, 3333), 3, 30, 3);
            List<Group> groups = Arrays.asList(group1, group2, group3);
            when(groupService.getAllGroups()).thenReturn(groups);
            GroupDto groupDto1 = new GroupDto(new UUID(1111, 1111), 1, 10, 1);
            GroupDto groupDto2 = new GroupDto(new UUID(2222, 2222), 2, 20, 2);
            GroupDto groupDto3 = new GroupDto(new UUID(3333, 3333), 3, 30, 3);

            when(groupMapper.groupToDto(group1)).thenReturn(groupDto1);
            when(groupMapper.groupToDto(group2)).thenReturn(groupDto2);
            when(groupMapper.groupToDto(group3)).thenReturn(groupDto3);


            mockMvc.perform(MockMvcRequestBuilders
                            .get("/groups")
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("[0].number", equalTo(1)))
                    .andExpect(jsonPath("[0].numberOfStudents", equalTo(10)))
                    .andExpect(jsonPath("[0].year", equalTo(1)))
                    .andExpect(jsonPath("[1].number", equalTo(2)))
                    .andExpect(jsonPath("[1].numberOfStudents", equalTo(20)))
                    .andExpect(jsonPath("[1].year", equalTo(2)))
                    .andExpect(jsonPath("[2].number", equalTo(3)))
                    .andExpect(jsonPath("[2].numberOfStudents", equalTo(30)))
                    .andExpect(jsonPath("[2].year", equalTo(3)));
        }

        @Test
        @WithMockUser
        @DisplayName("GET returns json empty array")
        public void getGroups_returnEmptyJsonArray() throws Exception {
            when(groupService.getAllGroups()).thenReturn(new ArrayList<>());

            mockMvc.perform(MockMvcRequestBuilders
                            .get("/groups")
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string("[]"));
        }

        @Test
        @WithAnonymousUser
        @DisplayName("GET returns 403")
        public void getGroups_executeWithUnknownUser() throws Exception {
            when(groupService.getAllGroups()).thenReturn(new ArrayList<>());

            mockMvc.perform(MockMvcRequestBuilders
                            .get("/groups")
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("getGroupById")
    class GetGroupById {

        @Test
        @WithMockUser
        public void getGroupById_returnUserWithThisId() throws Exception {
            Group group = new Group(UUID.randomUUID(), 1, 10, 1);

            when(groupService.getGroupById(any(UUID.class))).thenReturn(group);
            when(groupMapper.groupToDto(group)).thenReturn(new GroupDto(UUID.randomUUID(), 1, 10, 1));

            mockMvc.perform(MockMvcRequestBuilders
                            .get("/groups/123e4567-e89b-12d3-a456-426614174000")
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.number", equalTo(1)))
                    .andExpect(jsonPath("$.numberOfStudents", equalTo(10)))
                    .andExpect(jsonPath("$.year", equalTo(1)));
        }

        @Test
        @WithAnonymousUser
        @DisplayName("GET returns 403")
        public void getGroups_executeWithUnknownUser() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders
                            .get("/groups/123e4567-e89b-12d3-a456-426614174000")
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }

        //в случае если id неверен за счет хендлера у меня получается интеграционный тест и сейчас я его не тестирую
//        @Test
//        @WithMockUser
//        public void getGroupById_returnNotFoundMessage() throws Exception {
//
//            when(groupService.getGroupById(any(UUID.class))).thenThrow(new ItemNotFoundException("wrong id"));
//
//            mockMvc.perform(MockMvcRequestBuilders
//                            .get("/groups/123e4567-e89b-12d3-a456-426614174000")
//                            .accept(MediaType.APPLICATION_JSON))
//                    .andDo(print());
//        }

    }

    @Nested
    @DisplayName("addGroup")
    class AddGroup {

        @Test
        @WithMockUser(roles = "ADMIN")
        public void addGroup_returnAddedGroup() throws Exception {
            GroupDto groupDto = new GroupDto(UUID.randomUUID(), 1, 0, 1);
            Group group =new Group(UUID.randomUUID(),1,10,1);
            when(groupMapper.dtoToGroup(any(groupDto.getClass()))).thenReturn(group);
            when(groupService.addGroup(any(group.getClass()))).thenReturn(group);
            when(groupMapper.groupToDto(any(group.getClass()))).thenReturn(groupDto);

            mockMvc.perform(MockMvcRequestBuilders
                            .post("/groups")
                            .content("{\"number\":1,\"year\":1}")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.number", equalTo(1)))
                    .andExpect(jsonPath("$.numberOfStudents", equalTo(0)))
                    .andExpect(jsonPath("$.year", equalTo(1)));

        }

        @Test
        @WithMockUser(roles = {"TEACHER","STUDENT"})
        public void addGroup_return403() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders
                            .post("/groups")
                            .content("{\"number\":1,\"year\":1}")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isForbidden());

        }


    }

    @Nested
    @DisplayName("removeGroup")
    class RemoveGroup{

        @Test
        @WithMockUser(roles = "ADMIN")
        public void removeGroup_return202AndExecGroupServ() throws Exception {
            doNothing().when(groupService).removeGroup(any(UUID.class));
            mockMvc.perform(MockMvcRequestBuilders
                            .delete("/groups/123e4567-e89b-12d3-a456-426614174000")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
            verify(groupService,times(1)).removeGroup(any(UUID.class));
        }

        @Test
        @WithMockUser(roles = {"TEACHER","STUDENT"})
        public void removeGroup_return403() throws Exception {
            doNothing().when(groupService).removeGroup(any(UUID.class));
            mockMvc.perform(MockMvcRequestBuilders
                            .delete("/groups/123e4567-e89b-12d3-a456-426614174000")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isForbidden());
            verify(groupService,times(0)).removeGroup(any(UUID.class));
        }
    }

    @Nested
    @DisplayName("updateGroup")
    class UpdateGroup{

        @Test
        @WithMockUser(roles = "ADMIN")
        public void addGroup_returnAddedGroup() throws Exception {
            GroupDto groupDto = new GroupDto(UUID.randomUUID(), 5, 0, 5);
            Group group =new Group(UUID.randomUUID(),5,0,5);
            when(groupMapper.dtoToGroup(any(groupDto.getClass()))).thenReturn(group);
            when(groupService.updateGroup(any(group.getClass()))).thenReturn(group);
            when(groupMapper.groupToDto(any(group.getClass()))).thenReturn(groupDto);

            mockMvc.perform(MockMvcRequestBuilders
                            .put("/groups")
                            .content("{\"id\":\"123e4567-e89b-12d3-a456-426614174000\",\"number\":5,\"year\":5}")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.number", equalTo(5)))
                    .andExpect(jsonPath("$.numberOfStudents", equalTo(0)))
                    .andExpect(jsonPath("$.year", equalTo(5)));

        }

        @Test
        @WithMockUser(roles = {"TEACHER","STUDENT"})
        public void updateGroup_return403() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders
                            .put("/groups")
                            .content("{\"id\":\"123e4567-e89b-12d3-a456-426614174000\",\"number\":1,\"year\":1}")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isForbidden());

        }



    }
}
