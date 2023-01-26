package com.innowise.WinterProject.service;

import com.innowise.WinterProject.dto.StudentDto;
import com.innowise.WinterProject.dto.UserDto;
import com.innowise.WinterProject.entity.Group;
import com.innowise.WinterProject.entity.Role;
import com.innowise.WinterProject.entity.Student;
import com.innowise.WinterProject.entity.User;
import com.innowise.WinterProject.exeption.ItemNotFoundException;
import com.innowise.WinterProject.mapper.StudentMapper;
import com.innowise.WinterProject.mapper.UserMapper;
import com.innowise.WinterProject.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTests {
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private  StudentMapper studentMapper;
    @Mock
    private  GroupService groupService;
    @Mock
    private  UserService userService;
    @Mock
    private  UserMapper userMapper;
    @InjectMocks
    StudentService studentService;

    private static final UUID ID = UUID.randomUUID();
    private static final String FIRST_NAME = "Uliana";
    private static final String LAST_NAME  = "Uliana";
    private static final String LOGIN = "agent008";
    private static final String PASSWORD = "PolniKringe";

    @Test
    public void getAllStudentsTest() {
        Group group = Group.builder().id(UUID.randomUUID()).number(1).numberOfStudents(1).year(1).build();
        Student student1 = Student.builder().id(UUID.randomUUID()).group(group).firstName("Uliana").lastName("Usatova").build();
        Student student2 = Student.builder().id(UUID.randomUUID()).group(group).firstName("Nikto").lastName("Bezrabotni").build();
        Student student3 = Student.builder().id(UUID.randomUUID()).group(group).firstName("Jhon").lastName("Gold").build();

        List<Student> students = Arrays.asList(student1, student2, student3);
        when(studentRepository.findAll()).thenReturn(students);
        assertEquals(students, studentService.getAllStudents());

    }

    @Test
    public void getStudentByIdReturnStudentWithSuchId() {
        Group group = Group.builder().id(UUID.randomUUID()).number(1).numberOfStudents(1).year(1).build();
        Student student = Student.builder().id(UUID.randomUUID()).group(group).firstName(FIRST_NAME).lastName(LAST_NAME).build();

        when(studentRepository.findById(ID)).thenReturn(Optional.of(student));
        assertEquals(student, studentService.getStudentById(ID));
    }

    @Test
    public void getStudentByIdReturnExceptionOnWrongId() {
        when(studentRepository.findById(ID)).thenReturn(Optional.empty());
        assertThrows(ItemNotFoundException.class, () -> studentService.getStudentById(ID));
    }

    @Test
    public void createStudentReturnCreatedStudent() {
        Group group = Group.builder().id(ID).number(1).numberOfStudents(1).year(1).build();
        UserDto userDto = new UserDto(ID,LOGIN,LOGIN, Role.STUDENT);
        User user = new User(ID,LOGIN,LOGIN, Role.STUDENT,null,ID);
        StudentDto newStudent = new StudentDto(ID,group.getId(),FIRST_NAME,LAST_NAME,userDto);
        Student student = new Student(ID,group,FIRST_NAME,LAST_NAME);
        when(userMapper.dtoToUser(userDto)).thenReturn(user);
        when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn(PASSWORD);
        when(groupService.getGroupById(ID)).thenReturn(group);
        when(studentMapper.dtoToStudent(newStudent)).thenReturn(student);
        when(studentRepository.save(student)).thenReturn(student);
        when(userService.addUser(user)).thenReturn(user);

        assertEquals(student,studentService.createStudent(newStudent));
    }

    @Test
    public void removeStudentTest() {
        Group group = Group.builder().id(ID).number(1).numberOfStudents(1).year(1).build();
        User user = new User(ID,LOGIN,LOGIN, Role.STUDENT,null,ID);
        Student student = Student.builder().id(ID).group(group).firstName(FIRST_NAME).lastName(LAST_NAME).build();

        when(studentRepository.findById(ID)).thenReturn(Optional.of(student));
        when(userService.getUserByStudentId(student.getId())).thenReturn(user);

        studentService.removeStudent(student.getId());

        verify(groupService, times(1)).decreaseNumberOfStudentsInGroup(group);
        verify(userService,times(1)).removeUser(user.getId());
        verify(studentRepository,times(1)).deleteById(student.getId());
    }

    @Test
    public void updateStudentWithTheSameGroup() {
        Group group = Group.builder().id(UUID.randomUUID()).number(1).numberOfStudents(1).year(1).build();
        Student studentBeforeChanges = Student.builder().id(ID).group(group).firstName(FIRST_NAME).lastName(LAST_NAME).build();

        when(studentRepository.findById(ID)).thenReturn(Optional.of(studentBeforeChanges));

        Student studentChanges = Student.builder().id(ID).firstName("Tatiana").build();
        Student studentAfterChanges = Student.builder().id(ID).group(group).firstName("Tatiana").lastName(LAST_NAME).build();

        when(studentMapper.updateStudent(studentChanges,studentBeforeChanges)).thenReturn(studentAfterChanges);
        when(studentRepository.save(studentAfterChanges)).thenReturn(studentAfterChanges);

        assertEquals(studentAfterChanges,studentService.updateStudent(studentChanges));

    }

    @Test
    public void updateStudentChangeGroup() {
        Group group2 = Group.builder().id(ID).number(2).numberOfStudents(1).year(1).build();
        Student studentChanges = Student.builder().id(ID).firstName("Tatiana").group(group2).build();

        Group group1 = Group.builder().id(UUID.randomUUID()).number(1).numberOfStudents(1).year(1).build();
        Student studentBeforeChanges = Student.builder().id(ID).group(group1).firstName(FIRST_NAME).lastName(LAST_NAME).build();
        when(studentRepository.findById(studentChanges.getId())).thenReturn(Optional.of(studentBeforeChanges));

        Student studentAfterChanges = Student.builder().id(ID).group(group2).firstName("Tatiana").lastName(LAST_NAME).build();
        when(studentMapper.updateStudent(studentChanges,studentBeforeChanges)).thenReturn(studentAfterChanges);
        when(studentRepository.save(studentAfterChanges)).thenReturn(studentAfterChanges);

        assertEquals(studentAfterChanges,studentService.updateStudent(studentChanges));
        verify(groupService,times(1)).decreaseNumberOfStudentsInGroup(any(Group.class));
        verify(groupService,times(1)).increaseNumberOfStudentsInGroup(studentChanges.getGroup());


    }


}
