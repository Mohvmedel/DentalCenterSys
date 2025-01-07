package com.dentalcenter.appointment.dto;

import com.dentalcenter.appointment.model.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AppointmentStatusUpdateRequest {
    @NotNull(message = "Status is required")
    private AppointmentStatus status;
    
    private String notes;
} 