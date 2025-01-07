package com.dentalcenter.appointment.controller;

import com.dentalcenter.appointment.dto.*;
import com.dentalcenter.appointment.model.AppointmentStatus;
import com.dentalcenter.appointment.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentResponse createAppointment(@Valid @RequestBody AppointmentRequest request) {
        return appointmentService.createAppointment(request);
    }

    @GetMapping("/{id}")
    public AppointmentResponse getAppointment(@PathVariable Long id) {
        return appointmentService.getAppointment(id);
    }

    @GetMapping
    public List<AppointmentResponse> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/patient/{patientId}")
    public List<AppointmentResponse> getAppointmentsByPatient(@PathVariable Long patientId) {
        return appointmentService.getAppointmentsByPatient(patientId);
    }

    @GetMapping("/staff/{staffId}")
    public List<AppointmentResponse> getAppointmentsByStaff(@PathVariable Long staffId) {
        return appointmentService.getAppointmentsByStaff(staffId);
    }

    @GetMapping("/date-range")
    public List<AppointmentResponse> getAppointmentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return appointmentService.getAppointmentsByDateRange(start, end);
    }

    @GetMapping("/status/{status}")
    public List<AppointmentResponse> getAppointmentsByStatus(
            @PathVariable AppointmentStatus status) {
        return appointmentService.getAppointmentsByStatus(status);
    }

    @GetMapping("/upcoming")
    public List<AppointmentResponse> getUpcomingAppointments() {
        return appointmentService.getUpcomingAppointments();
    }

    @PutMapping("/{id}")
    public AppointmentResponse updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentUpdateRequest request) {
        return appointmentService.updateAppointment(id, request);
    }

    @PatchMapping("/{id}/status")
    public AppointmentResponse updateAppointmentStatus(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentStatusUpdateRequest request) {
        return appointmentService.updateAppointmentStatus(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
    }

    @GetMapping("/check-availability")
    public ResponseEntity<Boolean> checkTimeSlotAvailability(
            @RequestParam Long staffId,
            @RequestParam Long roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        boolean isAvailable = appointmentService.isTimeSlotAvailable(staffId, roomId, start, end);
        return ResponseEntity.ok(isAvailable);
    }
} 