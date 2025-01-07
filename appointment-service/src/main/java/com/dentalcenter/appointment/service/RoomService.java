package com.dentalcenter.appointment.service;

import com.dentalcenter.appointment.dto.RoomRequest;
import com.dentalcenter.appointment.dto.RoomResponse;
import com.dentalcenter.appointment.dto.RoomUpdateRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface RoomService {
    RoomResponse createRoom(RoomRequest request);
    
    RoomResponse getRoom(Long id);
    
    RoomResponse getRoomByNumber(String roomNumber);
    
    List<RoomResponse> getAllRooms();
    
    List<RoomResponse> getActiveRooms();
    
    List<RoomResponse> getRoomsByType(String type);
    
    List<RoomResponse> getAvailableRooms(LocalDateTime start, LocalDateTime end);
    
    RoomResponse updateRoom(Long id, RoomUpdateRequest request);
    
    void deleteRoom(Long id);
} 