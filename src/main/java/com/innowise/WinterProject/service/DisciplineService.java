package com.innowise.WinterProject.service;

import com.innowise.WinterProject.entity.Discipline;
import com.innowise.WinterProject.exeption.ItemNotFoundException;
import com.innowise.WinterProject.mapper.DisciplineMapper;
import com.innowise.WinterProject.repository.DisciplineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DisciplineService {

    private final DisciplineRepository disciplineRepository;
    private final DisciplineMapper disciplineMapper;

    public List<Discipline> getAllDisciplines() {
        return disciplineRepository.findAll();
    }

    public Discipline getDisciplineById(UUID id) {
        return disciplineRepository.findById(id).orElseThrow(() -> new ItemNotFoundException(id, Discipline.class));
    }


    public Discipline addDiscipline(Discipline newDiscipline) {
        return disciplineRepository.save(newDiscipline);
    }

    public void removeDiscipline(UUID id) {
        disciplineRepository.deleteById(id);
    }

    public Discipline updateDiscipline(Discipline disciplineAfterChanges) {
        return disciplineRepository.save(disciplineMapper.updateDiscipline(
                disciplineAfterChanges,getDisciplineById(disciplineAfterChanges.getId())));
    }
}
