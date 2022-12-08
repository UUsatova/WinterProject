package com.innowise.WinterProject.servise;

import com.innowise.WinterProject.entity.Room;
import com.innowise.WinterProject.exeption.WrongIdException;
import com.innowise.WinterProject.mapper.RoomMapper;
import com.innowise.WinterProject.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService {

    public final RoomRepository roomRepository;
    public final RoomMapper roomMapper;

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(UUID id) {
        return roomRepository.findById(id).orElseThrow(() -> new WrongIdException(id));
    }


    public Room addRoom(Room newStudent) {
        return roomRepository.save(newStudent);
    }

    public void removeRoom(UUID id) {
        roomRepository.deleteById(id);
    }

    public Room updateRoom(Room roomAfterChanges) {
        return roomRepository.save(roomMapper.updateRoom(
                getRoomById(roomAfterChanges.getId()), roomAfterChanges));
    }
}