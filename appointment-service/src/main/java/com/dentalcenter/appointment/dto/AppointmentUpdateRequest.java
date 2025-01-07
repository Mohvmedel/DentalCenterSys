package com.dentalcenter.appointment.dto;

import com.dentalcenter.appointment.model.AppointmentStatus;
import com.dentalcenter.appointment.model.AppointmentType;
import jakarta.validation.constraints.Future;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentUpdateRequest {
    private Long staffId;
    private Long roomId;
    private AppointmentType appointmentType;
    
    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;
    
    @Future(message = "End time must be in the future")
    private LocalDateTime endTime;
    
    private AppointmentStatus status;
    private String notes;
} 