package com.innowise.WinterProject.service;

import com.innowise.WinterProject.entity.Schedule;
import com.innowise.WinterProject.exeption.WrongIdException;
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

    public List<Schedule> getAllSchedule() {
        return scheduleRepository.findAll();
    }

    public Schedule getScheduleById(UUID id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new WrongIdException(id));
    }

    public Schedule addSchedule(Schedule newSchedule) {
        return scheduleRepository.save(newSchedule);
    }

    public void removeSchedule(UUID id) {
        scheduleRepository.deleteById(id);
    }

    public Schedule updateSchedule(Schedule scheduleAfterChanges) {
        Schedule scheduleBeforeChanges = getScheduleById(scheduleAfterChanges.getId());
        return scheduleRepository.save(scheduleMapper.updateSchedule(scheduleBeforeChanges, scheduleAfterChanges));
    }
}
