package com.innowise.WinterProject.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.WinterProject.dto.StudentDto;
import com.innowise.WinterProject.entity.Group;
import com.innowise.WinterProject.entity.Student;
import com.innowise.WinterProject.mapper.StudentMapper;
import com.innowise.WinterProject.mapper.UserMapper;
import com.innowise.WinterProject.repository.StudentRepository;
import com.innowise.WinterProject.repository.UserRepository;
import com.innowise.WinterProject.security.JwtFilter;
import com.innowise.WinterProject.security.JwtProvider;
import com.innowise.WinterProject.security.SecurityConfig;
import com.innowise.WinterProject.service.StudentService;
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

@WebMvcTest(controllers = {StudentsController.class})
@ContextConfiguration(classes = {StudentsController.class, SecurityConfig.class, JwtProvider.class, JwtFilter.class})
public class StudentsControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StudentService studentService;
    @MockBean
    private StudentMapper studentMapper;

    @MockBean
    private UserMapper userMapper;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    LoginExistInDataBase loginExistInDataBase;

    @MockBean
    UserRepository userRepository;

    @MockBean
    StudentRepository studentRepository;


    private static final String URL = "/students";
    private static final String URL_WITH_ID = "/students/123e4567-e89b-12d3-a456-426614174000";


    @Test
    @WithMockUser
    public void getStudentsReturnListOfStudentsInJsonArray() throws Exception {
        Group group  = new Group(UUID.randomUUID(),1,3,1);

        Student student1 = new Student(UUID.randomUUID(),group, "Pupic", "Rupic");
        Student student2 = new Student(UUID.randomUUID(),group, "Jopic", "Popic");
        Student student3 = new Student(UUID.randomUUID(),group,"Supic", "Fufic");
        List<Student> students = Arrays.asList(student1, student2, student3);

        when(studentService.getAllStudents()).thenReturn(students);
        StudentDto studentDto1 = new StudentDto(UUID.randomUUID(),group.getId(), "Pupic", "Rupic", null);
        StudentDto studentDto2 = new StudentDto(UUID.randomUUID(),group.getId() ,"Jopic", "Popic",null );
        StudentDto studentDto3 = new StudentDto(UUID.randomUUID(),group.getId(), "Supic", "Fufic", null);

        when(studentMapper.studentToDto(student1)).thenReturn(studentDto1);
        when(studentMapper.studentToDto(student2)).thenReturn(studentDto2);
        when(studentMapper.studentToDto(student3)).thenReturn(studentDto3);


        mockMvc.perform(MockMvcRequestBuilders.get("/students")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].firstName", equalTo(student1.getFirstName())))
                .andExpect(jsonPath("[0].lastName", equalTo(student1.getLastName())))
                .andExpect(jsonPath("[0].groupId", equalTo(student1.getGroup().getId().toString())))
                .andExpect(jsonPath("[1].firstName", equalTo(student2.getFirstName())))
                .andExpect(jsonPath("[1].lastName", equalTo(student2.getLastName())))
                .andExpect(jsonPath("[1].groupId", equalTo(student1.getGroup().getId().toString())))
                .andExpect(jsonPath("[2].firstName", equalTo(student3.getFirstName())))
                .andExpect(jsonPath("[2].lastName", equalTo(student3.getLastName())))
                .andExpect(jsonPath("[2].groupId", equalTo(student1.getGroup().getId().toString())));
    }

    @Test
    @WithAnonymousUser
    public void getStudentsExecuteWithUnknownUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    public void getTeacherByIdReturnUserByPassedId() throws Exception {
        Group group  = new Group(UUID.randomUUID(),1,1,1);
        Student student = new Student(UUID.randomUUID(),group ,"Pupic", "Rupic");

        when(studentService.getStudentById(any(UUID.class))).thenReturn(student);
        when(studentMapper.studentToDto(student)).thenReturn(new StudentDto(UUID.randomUUID(), group.getId(),"Pupic", "Rupic", null));

        mockMvc.perform(MockMvcRequestBuilders.get(URL_WITH_ID )
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo(student.getFirstName())))
                .andExpect(jsonPath("$.lastName", equalTo(student.getLastName())));
    }

    @Test
    @WithAnonymousUser
    public void getStudentByIdExecuteWithUnknownUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_WITH_ID )
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void createStudentReturnCreatedStudent() throws Exception {
        String dataInJson ="{\"firstName\":\"Pavlic\",\"lastName\":\"Morozov\",\"groupId\":\"a12059f9-f958-4364-9461-436dae1b1f4a\",\"userDto\":{\n\"login\":\"Chepushila\",\"password\":\"Chepushila\",\"role\":\"STUDENT\"}}";
        StudentDto studentDto = mapper.readValue(dataInJson, StudentDto.class);
        Student student = new Student(UUID.randomUUID(),new Group(UUID.randomUUID(),1,1,1), "Pavlic", "Morozov");

        when(studentService.createStudent(studentDto)).thenReturn(student);
        when(studentMapper.studentToDto(student)).thenReturn(studentDto);


        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(dataInJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo(student.getFirstName())))
                .andExpect(jsonPath("$.lastName", equalTo(student.getLastName())));

    }

    @Test
    @WithMockUser(roles = {"TEACHER", "STUDENT"})
    public void createStudentExecutedWithoutAdminRightsReturn403() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content("{\"firstName\":\"Pavlic\",\"lastName\":\"Morozov\",\"groupId\":\"a12059f9-f958-4364-9461-436dae1b1f4a\",\"userDto\":{\n\"login\":\"Chepushila\",\"password\":\"Chepushila\",\"role\":\"STUDENT\"}}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void removeTeacherReturn202AndExecuteTeacherService() throws Exception {
        doNothing().when(studentService).removeStudent(any(UUID.class));
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_WITH_ID )
                        .contentType(MediaType.APPLICATION_JSON).
                        accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        verify(studentService, times(1)).removeStudent(any(UUID.class));
    }

    @Test
    @WithMockUser(roles = {"TEACHER", "STUDENT"})
    public void removeTeacherExecutedWithoutAdminRightsReturn403() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_WITH_ID )
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
        verify(studentService, times(0)).removeStudent(any(UUID.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateStudentReturnUpdatedStudent() throws Exception {
        String dataInJson = "{\"id\":\"123e4567-e89b-12d3-a456-426614174000\",\"firstName\":\"Pavlic\",\"lastName\":\"Morozov\"}";
        StudentDto studentDto1 = mapper.readValue(dataInJson, StudentDto.class);
        Group group  = new Group(UUID.randomUUID(),1,1,1);
        Student student = new Student(UUID.randomUUID(), group ,"Pavlic", "Morozov");

        when(studentMapper.dtoToStudent(studentDto1)).thenReturn(student);
        when(studentService.updateStudent(student)).thenReturn(student);
        when(studentMapper.studentToDto(student)).thenReturn(studentDto1);
        when(studentRepository.existsById(any(UUID.class))).thenReturn(true);


        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content(dataInJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo(student.getFirstName())))
                .andExpect(jsonPath("$.lastName", equalTo(student.getLastName())));

    }

    @Test
    @WithMockUser(roles = {"TEACHER", "STUDENT"})
    public void updateStudentExecutedWithoutAdminRightsReturn403() throws Exception {
        when(studentRepository.existsById(any(UUID.class))).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content("{\"id\":\"123e4567-e89b-12d3-a456-426614174000\",\"firstName\":\"Pavlic\",\"lastName\":\"Morozov\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());

    }
}
