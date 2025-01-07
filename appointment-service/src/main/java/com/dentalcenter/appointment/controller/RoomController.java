package com.dentalcenter.appointment.controller;

import com.dentalcenter.appointment.dto.RoomRequest;
import com.dentalcenter.appointment.dto.RoomResponse;
import com.dentalcenter.appointment.dto.RoomUpdateRequest;
import com.dentalcenter.appointment.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoomResponse createRoom(@Valid @RequestBody RoomRequest request) {
        return roomService.createRoom(request);
    }

    @GetMapping("/{id}")
    public RoomResponse getRoom(@PathVariable Long id) {
        return roomService.getRoom(id);
    }

    @GetMapping("/number/{roomNumber}")
    public RoomResponse getRoomByNumber(@PathVariable String roomNumber) {
        return roomService.getRoomByNumber(roomNumber);
    }

    @GetMapping
    public List<RoomResponse> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/active")
    public List<RoomResponse> getActiveRooms() {
        return roomService.getActiveRooms();
    }

    @GetMapping("/type/{type}")
    public List<RoomResponse> getRoomsByType(@PathVariable String type) {
        return roomService.getRoomsByType(type);
    }

    @GetMapping("/available")
    public List<RoomResponse> getAvailableRooms(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return roomService.getAvailableRooms(start, end);
    }

    @PutMapping("/{id}")
    public RoomResponse updateRoom(
            @PathVariable Long id,
            @Valid @RequestBody RoomUpdateRequest request) {
        return roomService.updateRoom(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
    }
} 