package com.innowise.WinterProject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.WinterProject.dto.ScheduleDto;
import com.innowise.WinterProject.entity.Discipline;
import com.innowise.WinterProject.entity.Group;
import com.innowise.WinterProject.entity.Room;
import com.innowise.WinterProject.entity.Schedule;
import com.innowise.WinterProject.entity.Teacher;
import com.innowise.WinterProject.mapper.ScheduleMapper;
import com.innowise.WinterProject.repository.ScheduleRepository;
import com.innowise.WinterProject.security.JwtFilter;
import com.innowise.WinterProject.security.JwtProvider;
import com.innowise.WinterProject.security.SecurityConfig;
import com.innowise.WinterProject.service.ScheduleService;
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

import java.time.LocalDate;
import java.time.LocalTime;
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

@WebMvcTest(controllers = {ScheduleController.class})
@ContextConfiguration(classes = {ScheduleController.class, SecurityConfig.class, JwtProvider.class, JwtFilter.class})
public class ScheduleControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ScheduleService scheduleService;
    @MockBean
    private ScheduleMapper scheduleMapper;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    ScheduleRepository scheduleRepository;

    private static final String URL = "/schedule";
    private static final String URL_WITH_ID = "/schedule/123e4567-e89b-12d3-a456-426614174000";


    @Test
    @WithMockUser
    public void getScheduleReturnListOfScheduleInJsonArray() throws Exception {
        Group group = new Group(UUID.randomUUID(), 1, 10, 1);
        Room room = new Room(UUID.randomUUID(), 1, "one");
        Teacher teacher = new Teacher(UUID.randomUUID(), "Pupic", "Rupic");
        Discipline discipline = new Discipline(UUID.randomUUID(), "chijik pijik");
        LocalTime start = LocalTime.of(15,15);
        LocalTime end = LocalTime.of(18,15);
        LocalDate date = LocalDate.of(2002,8,26);

        Schedule schedule1 = new Schedule(UUID.randomUUID(), group, room, teacher,discipline,date,start,end);
        Schedule schedule2 = new Schedule(UUID.randomUUID(), group, room, teacher,discipline,date,start,end);
        Schedule schedule3 = new Schedule(UUID.randomUUID(), group, room, teacher,discipline,date,start,end);
        List<Schedule> schedules = Arrays.asList(schedule1, schedule2, schedule3);
        when(scheduleService.getAllSchedule()).thenReturn(schedules);
        ScheduleDto scheduleDto1 = new ScheduleDto(UUID.randomUUID(), group.getId(), room.getId(), teacher.getId(),discipline.getId(),date,start,end);
        ScheduleDto scheduleDto2 = new ScheduleDto(UUID.randomUUID(), group.getId(), room.getId(), teacher.getId(),discipline.getId(),date,start,end);
        ScheduleDto scheduleDto3 = new ScheduleDto(UUID.randomUUID(), group.getId(), room.getId(), teacher.getId(),discipline.getId(),date,start,end);

        when(scheduleMapper.scheduleToDto(schedule1)).thenReturn(scheduleDto1);
        when(scheduleMapper.scheduleToDto(schedule2)).thenReturn(scheduleDto2);
        when(scheduleMapper.scheduleToDto(schedule3)).thenReturn(scheduleDto3);


        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].groupId", equalTo(scheduleDto1.getGroupId().toString())))
                .andExpect(jsonPath("[0].roomId", equalTo(scheduleDto1.getRoomId().toString())))
                .andExpect(jsonPath("[0].teacherId", equalTo(scheduleDto1.getTeacherId().toString())))
                .andExpect(jsonPath("[0].disciplineId", equalTo(scheduleDto1.getDisciplineId().toString())))
                .andExpect(jsonPath("[0].date", equalTo(scheduleDto1.getDate().toString())))
                .andExpect(jsonPath("[1].groupId", equalTo(scheduleDto2.getGroupId().toString())))
                .andExpect(jsonPath("[1].roomId", equalTo(scheduleDto2.getRoomId().toString())))
                .andExpect(jsonPath("[1].teacherId", equalTo(scheduleDto2.getTeacherId().toString())))
                .andExpect(jsonPath("[1].disciplineId", equalTo(scheduleDto2.getDisciplineId().toString())))
                .andExpect(jsonPath("[1].date", equalTo(scheduleDto2.getDate().toString())))
                .andExpect(jsonPath("[2].groupId", equalTo(scheduleDto3.getGroupId().toString())))
                .andExpect(jsonPath("[2].roomId", equalTo(scheduleDto3.getRoomId().toString())))
                .andExpect(jsonPath("[2].teacherId", equalTo(scheduleDto3.getTeacherId().toString())))
                .andExpect(jsonPath("[2].disciplineId", equalTo(scheduleDto3.getDisciplineId().toString())))
                .andExpect(jsonPath("[2].date", equalTo(scheduleDto3.getDate().toString())));
    }

    @Test
    @WithAnonymousUser
    public void getScheduleExecuteWithUnknownUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    public void getScheduleByIdReturnScheduleByPassedId() throws Exception {
        Group group = new Group(UUID.randomUUID(), 1, 10, 1);
        Room room = new Room(UUID.randomUUID(), 1, "one");
        Teacher teacher = new Teacher(UUID.randomUUID(), "Pupic", "Rupic");
        Discipline discipline = new Discipline(UUID.randomUUID(), "chijik pijik");
        LocalTime start = LocalTime.of(15,15);
        LocalTime end = LocalTime.of(18,15);
        LocalDate date = LocalDate.of(2002,8,26);

        Schedule schedule = new Schedule(UUID.randomUUID(), group, room, teacher,discipline,date,start,end);
        ScheduleDto scheduleDto = new ScheduleDto(UUID.randomUUID(), group.getId(), room.getId(), teacher.getId(),discipline.getId(),date,start,end);


        when(scheduleService.getScheduleById(any(UUID.class))).thenReturn(schedule);
        when(scheduleMapper.scheduleToDto(schedule)).thenReturn(scheduleDto);
        mockMvc.perform(MockMvcRequestBuilders.get(URL_WITH_ID )
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupId", equalTo(scheduleDto.getGroupId().toString())))
                .andExpect(jsonPath("$.roomId", equalTo(scheduleDto.getRoomId().toString())))
                .andExpect(jsonPath("$.teacherId", equalTo(scheduleDto.getTeacherId().toString())))
                .andExpect(jsonPath("$.disciplineId", equalTo(scheduleDto.getDisciplineId().toString())))
                .andExpect(jsonPath("$.date", equalTo(scheduleDto.getDate().toString())));
    }


    @Test
    @WithAnonymousUser
    public void getScheduleByIdExecuteWithUnknownUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_WITH_ID )
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void addScheduleReturnAddedSchedule() throws Exception {
        String dataInJson = "{\"groupId\": \"a909d944-3bdf-45fc-93dc-4e1b30cefeb3\",\"roomId\": \"148ee56d-71db-45b9-b6ce-bdf057c19bf9\",\"teacherId\": \"49829cc1-af9e-41e6-903b-372bf2111722\",\"disciplineId\": \"eb8548ba-7204-45b7-bd0f-03ee790e51a4\",\"date\": \"2010-08-10\",\"startTime\": \"00:00:00\",\"endTime\": \"10:00:00\"}";
                Group group = new Group(UUID.randomUUID(), 1, 10, 1);
        Room room = new Room(UUID.randomUUID(), 1, "one");
        Teacher teacher = new Teacher(UUID.randomUUID(), "Pupic", "Rupic");
        Discipline discipline = new Discipline(UUID.randomUUID(), "chijik pijik");
        LocalTime start = LocalTime.of(15,15);
        LocalTime end = LocalTime.of(18,15);
        LocalDate date = LocalDate.of(2002,8,26);

        Schedule schedule = new Schedule(UUID.randomUUID(), group, room, teacher,discipline,date,start,end);
        ScheduleDto scheduleDto = mapper.readValue(dataInJson, ScheduleDto.class);
        when(scheduleService.addSchedule(scheduleDto)).thenReturn(schedule);
        when(scheduleMapper.scheduleToDto(schedule)).thenReturn(scheduleDto);

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(dataInJson).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupId", equalTo(scheduleDto.getGroupId().toString())))
                .andExpect(jsonPath("$.roomId", equalTo(scheduleDto.getRoomId().toString())))
                .andExpect(jsonPath("$.teacherId", equalTo(scheduleDto.getTeacherId().toString())))
                .andExpect(jsonPath("$.disciplineId", equalTo(scheduleDto.getDisciplineId().toString())))
                .andExpect(jsonPath("$.date", equalTo(scheduleDto.getDate().toString())));

    }

    @Test
    @WithMockUser(roles = {"TEACHER", "STUDENT"})
    public void addScheduleExecutedWithoutAdminRightsReturn403() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content("{\"groupId\": \"a909d944-3bdf-45fc-93dc-4e1b30cefeb3\",\"roomId\": \"148ee56d-71db-45b9-b6ce-bdf057c19bf9\",\"teacherId\": \"49829cc1-af9e-41e6-903b-372bf2111722\",\"disciplineId\": \"eb8548ba-7204-45b7-bd0f-03ee790e51a4\",\"date\": \"2010-08-10\",\"startTime\": \"00:00:00\",\"endTime\": \"10:00:00\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void removeGroupReturn202AndExecuteGroupService() throws Exception {
        doNothing().when(scheduleService).removeSchedule(any(UUID.class));
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_WITH_ID )
                        .contentType(MediaType.APPLICATION_JSON).
                        accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        verify(scheduleService, times(1)).removeSchedule(any(UUID.class));
    }

    @Test
    @WithMockUser(roles = {"TEACHER", "STUDENT"})
    public void removeGroupExecutedWithoutAdminRightsReturn403() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_WITH_ID )
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
        verify(scheduleService, times(0)).removeSchedule(any(UUID.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateScheduleReturnAddedSchedule() throws Exception {
        String dataInJson = "{\"id\":\"123e4567-e89b-12d3-a456-426614174000\",\"groupId\": \"a909d944-3bdf-45fc-93dc-4e1b30cefeb3\",\"roomId\": \"148ee56d-71db-45b9-b6ce-bdf057c19bf9\",\"teacherId\": \"49829cc1-af9e-41e6-903b-372bf2111722\",\"disciplineId\": \"eb8548ba-7204-45b7-bd0f-03ee790e51a4\",\"date\": \"2010-08-10\",\"startTime\": \"00:00:00\",\"endTime\": \"10:00:00\"}";

        Group group = new Group(UUID.randomUUID(), 1, 10, 1);
        Room room = new Room(UUID.randomUUID(), 1, "one");
        Teacher teacher = new Teacher(UUID.randomUUID(), "Pupic", "Rupic");
        Discipline discipline = new Discipline(UUID.randomUUID(), "chijik pijik");
        LocalTime start = LocalTime.of(15,15);
        LocalTime end = LocalTime.of(18,15);
        LocalDate date = LocalDate.of(2002,8,26);

        Schedule schedule = new Schedule(UUID.randomUUID(), group, room, teacher,discipline,date,start,end);
        ScheduleDto scheduleDto = mapper.readValue(dataInJson, ScheduleDto.class);


        when(scheduleMapper.dtoToSchedule(scheduleDto)).thenReturn(schedule);
        when(scheduleService.updateSchedule(schedule)).thenReturn(schedule);
        when(scheduleMapper.scheduleToDto(schedule)).thenReturn(scheduleDto);
        when(scheduleRepository.existsById(any(UUID.class))).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content(dataInJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.groupId", equalTo(scheduleDto.getGroupId().toString())))
                .andExpect(jsonPath("$.roomId", equalTo(scheduleDto.getRoomId().toString())))
                .andExpect(jsonPath("$.teacherId", equalTo(scheduleDto.getTeacherId().toString())))
                .andExpect(jsonPath("$.disciplineId", equalTo(scheduleDto.getDisciplineId().toString())))
                .andExpect(jsonPath("$.date", equalTo(scheduleDto.getDate().toString())));


    }

    @Test
    @WithMockUser(roles = {"TEACHER", "STUDENT"})
    public void updateScheduleExecutedWithoutAdminRightsReturn403() throws Exception {
        when(scheduleRepository.existsById(any(UUID.class))).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content("{\"id\":\"123e4567-e89b-12d3-a456-426614174000\",\"groupId\": \"a909d944-3bdf-45fc-93dc-4e1b30cefeb3\",\"roomId\": \"148ee56d-71db-45b9-b6ce-bdf057c19bf9\",\"teacherId\": \"49829cc1-af9e-41e6-903b-372bf2111722\",\"disciplineId\": \"eb8548ba-7204-45b7-bd0f-03ee790e51a4\",\"date\": \"2010-08-10\",\"startTime\": \"00:00:00\",\"endTime\": \"10:00:00\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());

    }
}
