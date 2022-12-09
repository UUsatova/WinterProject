package com.innowise.WinterProject.servise;

import com.innowise.WinterProject.entity.Group;
import com.innowise.WinterProject.exeption.WrongIdException;
import com.innowise.WinterProject.mapper.GroupMapper;
import com.innowise.WinterProject.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    private final StudentService studentService;

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Group getGroupById(UUID id) {
        return groupRepository.findById(id).orElseThrow(() -> new WrongIdException(id));
    }

    public Group addGroup(Group group) {
        return groupRepository.save(group);
    }

    public void removeGroup(UUID id) {      //валидация на наличие студентов в группе
        groupRepository.deleteById(id);
    }

    public Group updateGroup(Group groupAfterChanges) {      //точно так же смотря что апдейтим верноятно нужны изменения
        return groupRepository.save(groupMapper.updateGroup( //можно ли запретить апдейтить колличество людей в группе
                getGroupById(groupAfterChanges.getId()), groupAfterChanges));
    }

    public void increaseNumberOfStudentsInGroup(Group group) {
        group.setNumberOfStudents(group.getNumberOfStudents() + 1);
        groupRepository.save(group);
    }

    public void decreaseNumberOfStudentsInGroup(Group group) {
        group.setNumberOfStudents(group.getNumberOfStudents() - 1);
        groupRepository.save(group);
    }

}
