package com.dentalcenter.appointment.service.Impl;

import com.dentalcenter.appointment.dto.*;
import com.dentalcenter.appointment.entity.Appointment;
import com.dentalcenter.appointment.entity.Room;
import com.dentalcenter.appointment.exception.ResourceNotFoundException;
import com.dentalcenter.appointment.exception.ValidationException;
import com.dentalcenter.appointment.model.AppointmentStatus;
import com.dentalcenter.appointment.repository.AppointmentRepository;
import com.dentalcenter.appointment.repository.RoomRepository;
import com.dentalcenter.appointment.service.AppointmentService;
import com.dentalcenter.appointment.client.PatientClient;
import com.dentalcenter.appointment.client.StaffClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import feign.FeignException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final RoomRepository roomRepository;
    private final PatientClient patientClient;
    private final StaffClient staffClient;

    @Override
    public AppointmentResponse createAppointment(AppointmentRequest request) {
        try {
            // Check if staff is active
            if (!staffClient.isStaffActive(request.getStaffId())) {
                throw new ValidationException("Selected staff member is not active");
            }
            
            if (!patientClient.existsById(request.getPatientId())) {
                throw new ValidationException("Patient not found");
            }
            if (!staffClient.existsById(request.getStaffId())) {
                throw new ValidationException("Staff not found");
            }
        } catch (FeignException e) {
            log.error("Error calling service: {}", e.getMessage());
            throw new ValidationException("Unable to validate request: " + e.getMessage());
        }

        validateTimeSlot(request.getStartTime(), request.getEndTime());
        
        if (!isTimeSlotAvailable(request.getStaffId(), request.getRoomId(), 
                request.getStartTime(), request.getEndTime())) {
            throw new ValidationException("The selected time slot is not available");
        }

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        Appointment appointment = new Appointment();
        appointment.setPatientId(request.getPatientId());
        appointment.setStaffId(request.getStaffId());
        appointment.setRoom(room);
        appointment.setAppointmentType(request.getAppointmentType());
        appointment.setStartTime(request.getStartTime());
        appointment.setEndTime(request.getEndTime());
        appointment.setNotes(request.getNotes());
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        return mapToResponse(appointmentRepository.save(appointment));
    }

    @Override
    public AppointmentResponse getAppointment(Long id) {
        return mapToResponse(findAppointmentById(id));
    }

    @Override
    public List<AppointmentResponse> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponse> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponse> getAppointmentsByStaff(Long staffId) {
        return appointmentRepository.findByStaffId(staffId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponse> getAppointmentsByDateRange(LocalDateTime start, LocalDateTime end) {
        validateTimeSlot(start, end);
        return appointmentRepository.findByDateRange(start, end).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponse> getAppointmentsByStatus(AppointmentStatus status) {
        return appointmentRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponse> getUpcomingAppointments() {
        return appointmentRepository.findUpcomingAppointments(LocalDateTime.now()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentResponse updateAppointment(Long id, AppointmentUpdateRequest request) {
        Appointment appointment = findAppointmentById(id);

        if (request.getStartTime() != null && request.getEndTime() != null) {
            validateTimeSlot(request.getStartTime(), request.getEndTime());
            
            if (!isTimeSlotAvailable(
                    request.getStaffId() != null ? request.getStaffId() : appointment.getStaffId(),
                    request.getRoomId() != null ? request.getRoomId() : appointment.getRoom().getId(),
                    request.getStartTime(),
                    request.getEndTime())) {
                throw new ValidationException("The selected time slot is not available");
            }
            
            appointment.setStartTime(request.getStartTime());
            appointment.setEndTime(request.getEndTime());
        }

        if (request.getStaffId() != null) {
            appointment.setStaffId(request.getStaffId());
        }

        if (request.getRoomId() != null) {
            Room room = roomRepository.findById(request.getRoomId())
                    .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
            appointment.setRoom(room);
        }

        if (request.getAppointmentType() != null) {
            appointment.setAppointmentType(request.getAppointmentType());
        }

        if (request.getStatus() != null) {
            appointment.setStatus(request.getStatus());
        }

        if (request.getNotes() != null) {
            appointment.setNotes(request.getNotes());
        }

        return mapToResponse(appointmentRepository.save(appointment));
    }

    @Override
    public AppointmentResponse updateAppointmentStatus(Long id, AppointmentStatusUpdateRequest request) {
        Appointment appointment = findAppointmentById(id);
        appointment.setStatus(request.getStatus());
        
        if (request.getNotes() != null) {
            appointment.setNotes(request.getNotes());
        }

        return mapToResponse(appointmentRepository.save(appointment));
    }

    @Override
    public void cancelAppointment(Long id) {
        Appointment appointment = findAppointmentById(id);
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    @Override
    public boolean isTimeSlotAvailable(Long staffId, Long roomId, LocalDateTime start, LocalDateTime end) {
        log.info("Checking availability for staff:{}, room:{}, start:{}, end:{}", 
                staffId, roomId, start, end);
        
        try {
            validateTimeSlot(start, end);
            log.info("Time slot validation passed");
            
            // Check if staff exists
            boolean staffExists = staffClient.existsById(staffId);
            log.info("Staff exists check: {}", staffExists);
            if (!staffExists) {
                throw new ValidationException("Staff not found");
            }

            // Check if room exists
            boolean roomExists = roomRepository.existsById(roomId);
            log.info("Room exists check: {}", roomExists);
            if (!roomExists) {
                throw new ValidationException("Room not found");
            }

            // Check for overlapping appointments
            List<Appointment> staffConflicts = appointmentRepository
                    .findOverlappingAppointmentsForStaff(staffId, start, end);
            List<Appointment> roomConflicts = appointmentRepository
                    .findOverlappingAppointmentsForRoom(roomId, start, end);
            
            log.info("Staff conflicts: {}, Room conflicts: {}", 
                    staffConflicts.size(), roomConflicts.size());
            
            return staffConflicts.isEmpty() && roomConflicts.isEmpty();
            
        } catch (Exception e) {
            log.error("Error checking time slot availability", e);
            throw new ValidationException("Error checking availability: " + e.getMessage());
        }
    }

    private Appointment findAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
    }

    private void validateTimeSlot(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new ValidationException("Start time must be before end time");
        }
        
        LocalDateTime now = LocalDateTime.now();
        if (start.isBefore(now)) {
            throw new ValidationException("Start time must be in the future");
        }
    }

    private AppointmentResponse mapToResponse(Appointment appointment) {
        AppointmentResponse response = new AppointmentResponse();
        response.setId(appointment.getId());
        response.setPatientId(appointment.getPatientId());
        response.setStaffId(appointment.getStaffId());
        response.setRoom(mapRoomToResponse(appointment.getRoom()));
        response.setAppointmentType(appointment.getAppointmentType());
        response.setStartTime(appointment.getStartTime());
        response.setEndTime(appointment.getEndTime());
        response.setStatus(appointment.getStatus());
        response.setNotes(appointment.getNotes());
        response.setCreatedAt(appointment.getCreatedAt());
        response.setUpdatedAt(appointment.getUpdatedAt());
        return response;
    }

    private RoomResponse mapRoomToResponse(Room room) {
        RoomResponse response = new RoomResponse();
        response.setId(room.getId());
        response.setRoomNumber(room.getRoomNumber());
        response.setType(room.getType());
        response.setDescription(room.getDescription());
        response.setActive(room.isActive());
        return response;
    }
} 