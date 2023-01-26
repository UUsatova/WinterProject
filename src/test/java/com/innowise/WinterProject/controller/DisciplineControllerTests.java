package com.innowise.WinterProject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.WinterProject.dto.DisciplineDto;
import com.innowise.WinterProject.entity.Discipline;
import com.innowise.WinterProject.mapper.DisciplineMapper;
import com.innowise.WinterProject.security.JwtFilter;
import com.innowise.WinterProject.security.JwtProvider;
import com.innowise.WinterProject.security.SecurityConfig;
import com.innowise.WinterProject.service.DisciplineService;
import org.junit.jupiter.api.DisplayName;
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

@WebMvcTest(controllers = {DisciplineController.class})
@ContextConfiguration(classes = {DisciplineController.class, SecurityConfig.class, JwtProvider.class, JwtFilter.class})

public class DisciplineControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DisciplineService disciplineService;
    @MockBean
    private DisciplineMapper disciplineMapper;
    @Autowired
    ObjectMapper mapper;

    private static final String URL  = "/disciplines";
    private static final String URL_WITH_ID  = "/disciplines/123e4567-e89b-12d3-a456-426614174000";


    @Test
    @WithMockUser
    public void getDisciplineReturnListOfDisciplinesInJsonArray() throws Exception {

        Discipline discipline1 = new Discipline(UUID.randomUUID(), "chijik pijik");
        Discipline discipline2 = new Discipline(UUID.randomUUID(), "kabanjik");
        Discipline discipline3 = new Discipline(UUID.randomUUID(), "soneika");
        List<Discipline> groups = Arrays.asList(discipline1, discipline2, discipline3);
        when(disciplineService.getAllDisciplines()).thenReturn(groups);
        DisciplineDto disciplineDto1 = new DisciplineDto(UUID.randomUUID(), "chijik pijik");
        DisciplineDto disciplineDto2 = new DisciplineDto(UUID.randomUUID(), "kabanjik");
        DisciplineDto disciplineDto3 = new DisciplineDto(UUID.randomUUID(), "soneika");

        when(disciplineMapper.disciplineToDto(discipline1)).thenReturn(disciplineDto1);
        when(disciplineMapper.disciplineToDto(discipline2)).thenReturn(disciplineDto2);
        when(disciplineMapper.disciplineToDto(discipline3)).thenReturn(disciplineDto3);


        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].name", equalTo(discipline1.getName())))
                .andExpect(jsonPath("[1].name", equalTo(discipline2.getName())))
                .andExpect(jsonPath("[2].name", equalTo(discipline3.getName())));
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
        Discipline discipline = new Discipline(UUID.randomUUID(), "chijik pijik");

        when(disciplineService.getDisciplineById(any(UUID.class))).thenReturn(discipline);
        when(disciplineMapper.disciplineToDto(discipline)).thenReturn(new DisciplineDto(UUID.randomUUID(), "chijik pijik"));

        mockMvc.perform(MockMvcRequestBuilders.get(URL_WITH_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(discipline.getName())));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("GET returns 403")
    public void getGroupById_executeWithUnknownUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_WITH_ID)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void addDisciplineReturnAddedDiscipline() throws Exception {
        String dataInJson = "{\"name\":\"boojy\"}";
        DisciplineDto disciplineDto = mapper.readValue(dataInJson, DisciplineDto.class);
        Discipline discipline = new Discipline(UUID.randomUUID(), "boojy");

        when(disciplineMapper.dtoToDiscipline(disciplineDto)).thenReturn(discipline);
        when(disciplineService.addDiscipline(discipline)).thenReturn(discipline);
        when(disciplineMapper.disciplineToDto(discipline)).thenReturn(disciplineDto);

        mockMvc.perform(MockMvcRequestBuilders.post(URL).content(dataInJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(discipline.getName())));
    }

    @Test
    @WithMockUser(roles = {"TEACHER", "STUDENT"})
    public void addDisciplineExecutedWithoutAdminRightsReturn403() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content("{\"name\":\"boojy\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void removeDisciplineReturn202AndExecuteDisciplineService() throws Exception {
        doNothing().when(disciplineService).removeDiscipline(any(UUID.class));
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_WITH_ID)
                        .contentType(MediaType.APPLICATION_JSON).
                        accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        verify(disciplineService, times(1)).removeDiscipline(any(UUID.class));
    }

    @Test
    @WithMockUser(roles = {"TEACHER", "STUDENT"})
    public void removeDisciplineExecutedWithoutAdminRightsReturn403() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_WITH_ID)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
        verify(disciplineService, times(0)).removeDiscipline(any(UUID.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateDisciplineReturnAddedGroup() throws Exception {
        String dataInJson = "{\"id\":\"123e4567-e89b-12d3-a456-426614174000\",\"name\":\"haha\"}";
        DisciplineDto disciplineDto = mapper.readValue(dataInJson, DisciplineDto.class);
        Discipline discipline = new Discipline(UUID.randomUUID(), "haha");

        when(disciplineMapper.dtoToDiscipline(disciplineDto)).thenReturn(discipline);
        when(disciplineService.updateDiscipline(discipline)).thenReturn(discipline);
        when(disciplineMapper.disciplineToDto(discipline)).thenReturn(disciplineDto);

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                .content(dataInJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(discipline.getName())));

    }

    @Test
    @WithMockUser(roles = {"TEACHER", "STUDENT"})
    public void updateDisciplineExecutedWithoutAdminRightsReturn403() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content("{\"id\":\"123e4567-e89b-12d3-a456-426614174000\",\"name\":\"haha\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());

    }
}
