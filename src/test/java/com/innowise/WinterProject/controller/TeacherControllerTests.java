package com.innowise.WinterProject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.WinterProject.dto.TeacherDto;
import com.innowise.WinterProject.dto.UserDto;
import com.innowise.WinterProject.entity.Role;
import com.innowise.WinterProject.entity.Teacher;
import com.innowise.WinterProject.entity.User;
import com.innowise.WinterProject.mapper.TeacherMapper;
import com.innowise.WinterProject.mapper.UserMapper;
import com.innowise.WinterProject.repository.UserRepository;
import com.innowise.WinterProject.security.JwtFilter;
import com.innowise.WinterProject.security.JwtProvider;
import com.innowise.WinterProject.security.SecurityConfig;
import com.innowise.WinterProject.service.TeacherService;
import com.innowise.WinterProject.validation.LoginExistInDataBase;
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

@WebMvcTest(controllers = {TeachersController.class})
@ContextConfiguration(classes = {TeachersController.class, SecurityConfig.class, JwtProvider.class, JwtFilter.class})
public class TeacherControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TeacherService teacherService;
    @MockBean
    private TeacherMapper teacherMapper;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserMapper userMapper;

    @MockBean
    LoginExistInDataBase loginExistInDataBase;
    @MockBean
    UserRepository userRepository;

    @Test
    @WithMockUser
    public void getTeachersReturnListOfTeachersInJsonArray() throws Exception {

        Teacher teacher1 = new Teacher(UUID.randomUUID(), "Pupic", "Rupic");
        Teacher teacher2 = new Teacher(UUID.randomUUID(), "Jopic", "Popic");
        Teacher teacher3 = new Teacher(UUID.randomUUID(), "Supic", "Fufic");
        List<Teacher> teachers = Arrays.asList(teacher1, teacher2, teacher3);

        when(teacherService.getAllTeachers()).thenReturn(teachers);
        TeacherDto teacherDto1 = new TeacherDto(UUID.randomUUID(), "Pupic", "Rupic", null);
        TeacherDto teacherDto2 = new TeacherDto(UUID.randomUUID(), "Jopic", "Popic",null );
        TeacherDto teacherDto3 = new TeacherDto(UUID.randomUUID(), "Supic", "Fufic", null);

        when(teacherMapper.teacherToDto(teacher1)).thenReturn(teacherDto1);
        when(teacherMapper.teacherToDto(teacher2)).thenReturn(teacherDto2);
        when(teacherMapper.teacherToDto(teacher3)).thenReturn(teacherDto3);


        mockMvc.perform(MockMvcRequestBuilders.get("/teachers")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].firstName", equalTo(teacher1.getFirstName())))
                .andExpect(jsonPath("[0].lastName", equalTo(teacher1.getLastName())))
                .andExpect(jsonPath("[1].firstName", equalTo(teacher2.getFirstName())))
                .andExpect(jsonPath("[1].lastName", equalTo(teacher2.getLastName())))
                .andExpect(jsonPath("[2].firstName", equalTo(teacher3.getFirstName())))
                .andExpect(jsonPath("[2].lastName", equalTo(teacher3.getLastName())));
    }

    @Test
    @WithAnonymousUser
    public void getTeachersExecuteWithUnknownUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/teachers")
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    public void getTeacherByIdReturnUserByPassedId() throws Exception {
        Teacher teacher = new Teacher(UUID.randomUUID(), "Pupic", "Rupic");

        when(teacherService.getTeacherById(any(UUID.class))).thenReturn(teacher);
        when(teacherMapper.teacherToDto(teacher)).thenReturn(new TeacherDto(UUID.randomUUID(), "Pupic", "Rupic", null));

        mockMvc.perform(MockMvcRequestBuilders.get("/teachers/123e4567-e89b-12d3-a456-426614174000")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo(teacher.getFirstName())))
                .andExpect(jsonPath("$.lastName", equalTo(teacher.getLastName())));
    }

    @Test
    @WithAnonymousUser
    public void getTeacherByIdExecuteWithUnknownUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/teachers/123e4567-e89b-12d3-a456-426614174000")
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void createTeacherReturnCreatedTeacher() throws Exception {
        TeacherDto teacherDto1 = mapper.readValue("{\"firstName\":\"Elena\",\"lastName\":\"Cheb\",\"userDto\":{\n\"login\":\"Chepushila\",\"password\":\"Chepushila\",\"role\":\"TEACHER\"}}", TeacherDto.class);
        Teacher teacher = new Teacher(UUID.randomUUID(), "Elena", "Cheb");

        UserDto userDto = mapper.readValue("{\"login\":\"Chepushila\",\"password\":\"Chepushila\",\"role\":\"TEACHER\"}",UserDto.class);
        User user = new User(UUID.randomUUID(),"Chepushila","Chepushila", Role.TEACHER,UUID.randomUUID(),null);

        TeacherDto teacherDto2 = new TeacherDto(UUID.randomUUID(), "Elena", "Cheb", null);

        when(teacherMapper.dtoToTeacher(teacherDto1)).thenReturn(teacher);
        when(userMapper.dtoToUser(userDto)).thenReturn(user);
        when(teacherService.createTeacher(teacher,user)).thenReturn(teacher);
        when(teacherMapper.teacherToDto(teacher)).thenReturn(teacherDto2);


        mockMvc.perform(MockMvcRequestBuilders.post("/teachers")
                .content("{\"firstName\":\"Elena\",\"lastName\":\"Cheb\",\"userDto\":{\n\"login\":\"Chepushila\",\"password\":\"Chepushila\",\"role\":\"TEACHER\"}}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo(teacher.getFirstName())))
                .andExpect(jsonPath("$.lastName", equalTo(teacher.getLastName())));

    }

    @Test
    @WithMockUser(roles = {"TEACHER", "STUDENT"})
    public void createTeacherExecutedWithoutAdminRightsReturn403() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/teachers")
                        .content("{\"firstName\":\"Elena\",\"lastName\":\"Cheb\",\"userDto\":{\n\"login\":\"Chepushila\",\"password\":\"Chepushila\",\"role\":\"TEACHER\"}}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void removeTeacherReturn202AndExecuteTeacherService() throws Exception {
        doNothing().when(teacherService).removeTeacher(any(UUID.class));
        mockMvc.perform(MockMvcRequestBuilders.delete("/teachers/123e4567-e89b-12d3-a456-426614174000")
                        .contentType(MediaType.APPLICATION_JSON).
                        accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        verify(teacherService, times(1)).removeTeacher(any(UUID.class));
    }

    @Test
    @WithMockUser(roles = {"TEACHER", "STUDENT"})
    public void removeTeacherExecutedWithoutAdminRightsReturn403() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/teachers/123e4567-e89b-12d3-a456-426614174000")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
        verify(teacherService, times(0)).removeTeacher(any(UUID.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateTeacherReturnUpdatedTeacher() throws Exception {
        TeacherDto teacherDto1 = mapper.readValue("{\"id\":\"123e4567-e89b-12d3-a456-426614174000\",\"firstName\":\"Elena\",\"lastName\":\"Cheb\"}", TeacherDto.class);
        Teacher teacher = new Teacher(UUID.randomUUID(), "Elena", "Cheb");

        TeacherDto teacherDto2 = new TeacherDto(UUID.randomUUID(), "Elena", "Cheb", null);

        when(teacherMapper.dtoToTeacher(teacherDto1)).thenReturn(teacher);
        when(teacherService.updateTeacher(teacher)).thenReturn(teacher);
        when(teacherMapper.teacherToDto(teacher)).thenReturn(teacherDto2);


        mockMvc.perform(MockMvcRequestBuilders.put("/teachers")
                        .content("{\"id\":\"123e4567-e89b-12d3-a456-426614174000\",\"firstName\":\"Elena\",\"lastName\":\"Cheb\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo(teacher.getFirstName())))
                .andExpect(jsonPath("$.lastName", equalTo(teacher.getLastName())));

    }

    @Test
    @WithMockUser(roles = {"TEACHER", "STUDENT"})
    public void updateTeacherExecutedWithoutAdminRightsReturn403() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/teachers")
                        .content("{\"id\":\"123e4567-e89b-12d3-a456-426614174000\",\"firstName\":\"Elena\",\"lastName\":\"Cheb\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());

    }
}
