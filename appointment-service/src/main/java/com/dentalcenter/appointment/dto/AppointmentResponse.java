package com.dentalcenter.appointment.dto;

import com.dentalcenter.appointment.model.AppointmentStatus;
import com.dentalcenter.appointment.model.AppointmentType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentResponse {
    private Long id;
    private Long patientId;
    private Long staffId;
    private RoomResponse room;
    private AppointmentType appointmentType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private AppointmentStatus status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}