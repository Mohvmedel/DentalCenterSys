package com.dentalcenter.appointment.service;

import com.dentalcenter.appointment.dto.AppointmentRequest;
import com.dentalcenter.appointment.dto.AppointmentResponse;
import com.dentalcenter.appointment.dto.AppointmentStatusUpdateRequest;
import com.dentalcenter.appointment.dto.AppointmentUpdateRequest;
import com.dentalcenter.appointment.model.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    AppointmentResponse createAppointment(AppointmentRequest request);
    
    AppointmentResponse getAppointment(Long id);
    
    List<AppointmentResponse> getAllAppointments();
    
    List<AppointmentResponse> getAppointmentsByPatient(Long patientId);
    
    List<AppointmentResponse> getAppointmentsByStaff(Long staffId);
    
    List<AppointmentResponse> getAppointmentsByDateRange(LocalDateTime start, LocalDateTime end);
    
    List<AppointmentResponse> getAppointmentsByStatus(AppointmentStatus status);
    
    List<AppointmentResponse> getUpcomingAppointments();
    
    AppointmentResponse updateAppointment(Long id, AppointmentUpdateRequest request);
    
    AppointmentResponse updateAppointmentStatus(Long id, AppointmentStatusUpdateRequest request);
    
    void cancelAppointment(Long id);
    
    boolean isTimeSlotAvailable(Long staffId, Long roomId, LocalDateTime start, LocalDateTime end);
} 