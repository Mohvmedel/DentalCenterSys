package com.dentalcenter.appointment.dto;

import com.dentalcenter.appointment.model.AppointmentType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentRequest {
    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Staff ID is required")
    private Long staffId;

    @NotNull(message = "Room ID is required")
    private Long roomId;

    @NotNull(message = "Appointment type is required")
    private AppointmentType appointmentType;

    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @Future(message = "End time must be in the future")
    private LocalDateTime endTime;

    private String notes;
}