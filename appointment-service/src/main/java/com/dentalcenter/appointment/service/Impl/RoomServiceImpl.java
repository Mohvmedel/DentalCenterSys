package com.dentalcenter.appointment.service.Impl;

import com.dentalcenter.appointment.dto.RoomRequest;
import com.dentalcenter.appointment.dto.RoomResponse;
import com.dentalcenter.appointment.dto.RoomUpdateRequest;
import com.dentalcenter.appointment.entity.Room;
import com.dentalcenter.appointment.exception.ResourceNotFoundException;
import com.dentalcenter.appointment.exception.ValidationException;
import com.dentalcenter.appointment.repository.RoomRepository;
import com.dentalcenter.appointment.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    public RoomResponse createRoom(RoomRequest request) {
        if (roomRepository.existsByRoomNumber(request.getRoomNumber())) {
            throw new ValidationException("Room number already exists");
        }

        Room room = new Room();
        room.setRoomNumber(request.getRoomNumber());
        room.setType(request.getType());
        room.setDescription(request.getDescription());
        room.setActive(request.isActive());

        return mapToResponse(roomRepository.save(room));
    }

    @Override
    public RoomResponse getRoom(Long id) {
        return mapToResponse(findRoomById(id));
    }

    @Override
    public RoomResponse getRoomByNumber(String roomNumber) {
        return roomRepository.findByRoomNumber(roomNumber)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
    }

    @Override
    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomResponse> getActiveRooms() {
        return roomRepository.findByActiveTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomResponse> getRoomsByType(String type) {
        return roomRepository.findByType(type).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomResponse> getAvailableRooms(LocalDateTime start, LocalDateTime end) {
        validateTimeRange(start, end);
        return roomRepository.findAvailableRooms(start, end).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RoomResponse updateRoom(Long id, RoomUpdateRequest request) {
        Room room = findRoomById(id);

        if (request.getRoomNumber() != null) {
            if (roomRepository.existsByRoomNumberAndIdNot(request.getRoomNumber(), id)) {
                throw new ValidationException("Room number already exists");
            }
            room.setRoomNumber(request.getRoomNumber());
        }

        if (request.getType() != null) {
            room.setType(request.getType());
        }

        if (request.getDescription() != null) {
            room.setDescription(request.getDescription());
        }

        if (request.getActive() != null) {
            room.setActive(request.getActive());
        }

        return mapToResponse(roomRepository.save(room));
    }

    @Override
    public void deleteRoom(Long id) {
        Room room = findRoomById(id);
        roomRepository.delete(room);
    }

    private Room findRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
    }

    private void validateTimeRange(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new ValidationException("Start time must be before end time");
        }
        if (start.isBefore(LocalDateTime.now())) {
            throw new ValidationException("Start time must be in the future");
        }
    }

    private RoomResponse mapToResponse(Room room) {
        RoomResponse response = new RoomResponse();
        response.setId(room.getId());
        response.setRoomNumber(room.getRoomNumber());
        response.setType(room.getType());
        response.setDescription(room.getDescription());
        response.setActive(room.isActive());
        response.setCreatedAt(room.getCreatedAt());
        response.setUpdatedAt(room.getUpdatedAt());
        return response;
    }
} 