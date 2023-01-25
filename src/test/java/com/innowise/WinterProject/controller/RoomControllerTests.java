package com.innowise.WinterProject.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.WinterProject.dto.RoomDto;
import com.innowise.WinterProject.entity.Room;
import com.innowise.WinterProject.mapper.RoomMapper;
import com.innowise.WinterProject.security.JwtFilter;
import com.innowise.WinterProject.security.JwtProvider;
import com.innowise.WinterProject.security.SecurityConfig;
import com.innowise.WinterProject.service.RoomService;
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

import java.util.ArrayList;
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

@WebMvcTest(controllers = {RoomController.class})
@ContextConfiguration(classes = {RoomController.class, SecurityConfig.class, JwtProvider.class, JwtFilter.class})
public class RoomControllerTests {


    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RoomService roomService;
    @MockBean
    private RoomMapper roomMapper;
    @Autowired
    ObjectMapper mapper;


    @Test
    @WithMockUser
    public void getRoomsReturnListOfRoomsInJsonArray() throws Exception {
        Room room1 = new Room(UUID.randomUUID(), 1, "one");
        Room room2 = new Room(UUID.randomUUID(), 2, "oneMore");
        Room room3 = new Room(UUID.randomUUID(), 3, "vulitsaJulitsa");

        List<Room> rooms = Arrays.asList(room1, room2, room3);
        when(roomService.getAllRooms()).thenReturn(rooms);
        RoomDto roomDto1 = new RoomDto(UUID.randomUUID(), 1, "one");
        RoomDto roomDto2 = new RoomDto(UUID.randomUUID(), 2, "oneMore");
        RoomDto roomDto3 = new RoomDto(UUID.randomUUID(), 3, "vulitsaJulitsa");

        when(roomMapper.roomToDto(room1)).thenReturn(roomDto1);
        when(roomMapper.roomToDto(room2)).thenReturn(roomDto2);
        when(roomMapper.roomToDto(room3)).thenReturn(roomDto3);


        mockMvc.perform(MockMvcRequestBuilders.get("/rooms")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).
                andExpect(jsonPath("[0].number", equalTo(room1.getNumber())))
                .andExpect(jsonPath("[0].address", equalTo(room1.getAddress())))
                .andExpect(jsonPath("[1].number", equalTo(room2.getNumber())))
                .andExpect(jsonPath("[1].address", equalTo(room2.getAddress())))
                .andExpect(jsonPath("[2].number", equalTo(room3.getNumber())))
                .andExpect(jsonPath("[2].address", equalTo(room3.getAddress())));
    }

    @Test
    @WithAnonymousUser
    public void getRoomsExecuteWithUnknownUser() throws Exception {
        when(roomService.getAllRooms()).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get("/rooms")
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    public void getRoomByIdReturnUserByPassedId() throws Exception {
        Room room = new Room(UUID.randomUUID(), 1, "one");

        when(roomService.getRoomById(any(UUID.class))).thenReturn(room);
        when(roomMapper.roomToDto(room)).thenReturn(new RoomDto(UUID.randomUUID(), 1, "one"));

        mockMvc.perform(MockMvcRequestBuilders.get("/rooms/123e4567-e89b-12d3-a456-426614174000")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number", equalTo(room.getNumber())))
                .andExpect(jsonPath("$.address", equalTo(room.getAddress())));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void addRoomReturnAddedRoom() throws Exception {
        RoomDto roomDto1 = mapper.readValue("{\"number\":1,\"address\":\"one\"}", RoomDto.class);
        Room room = new Room(UUID.randomUUID(), 1, "one");
        RoomDto roomDto2 = new RoomDto(UUID.randomUUID(), 1, "one");

        when(roomMapper.dtoToRoom(roomDto1)).thenReturn(room);
        when(roomService.addRoom(room)).thenReturn(room);
        when(roomMapper.roomToDto(room)).thenReturn(roomDto2);

        mockMvc.perform(MockMvcRequestBuilders.post("/rooms")
                        .content("{\"number\":1,\"address\":\"one\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number", equalTo(room.getNumber())))
                .andExpect(jsonPath("$.address", equalTo(room.getAddress())));

    }

    @Test
    @WithMockUser(roles = {"TEACHER", "STUDENT"})
    public void addRoomExecutedWithoutAdminRightsReturn403() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/rooms")
                        .content("{\"number\":1,\"address\":\"one\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void removeRoomReturn202AndExecuteRoomService() throws Exception {
        doNothing().when(roomService).removeRoom(any(UUID.class));
        mockMvc.perform(MockMvcRequestBuilders.delete("/rooms/123e4567-e89b-12d3-a456-426614174000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        verify(roomService, times(1)).removeRoom(any(UUID.class));
    }

    @Test
    @WithMockUser(roles = {"TEACHER", "STUDENT"})
    public void removeGroupExecutedWithoutAdminRightsReturn403() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/rooms/123e4567-e89b-12d3-a456-426614174000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
        verify(roomService, times(0)).removeRoom(any(UUID.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateGroupReturnAddedGroup() throws Exception {
        RoomDto roomDto1 = mapper.readValue("{\"id\":\"123e4567-e89b-12d3-a456-426614174000\",\"number\":5}", RoomDto.class);
        Room room = new Room(UUID.randomUUID(), 5, "one");
        RoomDto roomDto2 = new RoomDto(UUID.randomUUID(), 5, "one");

        when(roomMapper.dtoToRoom(roomDto1)).thenReturn(room);
        when(roomService.updateRoom(room)).thenReturn(room);
        when(roomMapper.roomToDto(room)).thenReturn(roomDto2);

        mockMvc.perform(MockMvcRequestBuilders.put("/rooms")
                        .content("{\"id\":\"123e4567-e89b-12d3-a456-426614174000\",\"number\":5}")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number", equalTo(room.getNumber()))).
                andExpect(jsonPath("$.address", equalTo(room.getAddress())));

    }


    @Test
    @WithMockUser(roles = {"TEACHER", "STUDENT"})
    public void updateRoomExecutedWithoutAdminRightsReturn403() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/rooms")
                        .content("{\"id\":\"123e4567-e89b-12d3-a456-426614174000\",\"number\":1}")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());

    }
}

