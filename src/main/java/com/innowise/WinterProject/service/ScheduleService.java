package com.innowise.WinterProject.service;

import com.innowise.WinterProject.dto.ScheduleDto;
import com.innowise.WinterProject.entity.*;
import com.innowise.WinterProject.exeption.ItemNotFoundException;
import com.innowise.WinterProject.mapper.ScheduleMapper;
import com.innowise.WinterProject.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;
    private final GroupService groupService;
    private final  RoomService roomService;
    private final DisciplineService disciplineService;
    private  final  TeacherService teacherService;


    public List<Schedule> getAllSchedule() {
        return scheduleRepository.findAll();
    }

    public Schedule getScheduleById(UUID id) {
        Schedule schedule = scheduleRepository.findById(id).get();
        return scheduleRepository.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
    }

    public Schedule addSchedule(ScheduleDto scheduleDto) {
        Group group = groupService.getGroupById(scheduleDto.getGroupId());
        Room room =roomService.getRoomById( scheduleDto.getRoomId());
        Discipline discipline = disciplineService.getDisciplineById( scheduleDto.getDisciplineId());
        Teacher teacher = teacherService.getTeacherById( scheduleDto.getTeacherId());

        Schedule schedule = scheduleMapper.dtoToSchedule(scheduleDto);
        schedule.setGroup(group);
        schedule.setRoom(room);
        schedule.setDiscipline(discipline);
        schedule.setTeacher(teacher);

        return scheduleRepository.save(schedule);
    }

    public void removeSchedule(UUID id) {
        scheduleRepository.deleteById(id);
    }

    public Schedule updateSchedule(Schedule scheduleAfterChanges) {
        Schedule scheduleBeforeChanges = getScheduleById(scheduleAfterChanges.getId());
        return scheduleRepository.save(scheduleMapper.updateSchedule(scheduleAfterChanges,scheduleBeforeChanges ));
    }
}
