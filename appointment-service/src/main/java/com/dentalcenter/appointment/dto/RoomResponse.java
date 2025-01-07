package com.dentalcenter.appointment.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RoomResponse {
    private Long id;
    private String roomNumber;
    private String type;
    private String description;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 