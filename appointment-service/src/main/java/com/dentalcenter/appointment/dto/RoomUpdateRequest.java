package com.dentalcenter.appointment.dto;

import lombok.Data;

@Data
public class RoomUpdateRequest {
    private String roomNumber;
    private String type;
    private String description;
    private Boolean active;
} 