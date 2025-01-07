package com.dentalcenter.appointment.repository;

import com.dentalcenter.appointment.entity.Appointment;
import com.dentalcenter.appointment.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    List<Appointment> findByPatientId(Long patientId);
    
    List<Appointment> findByStaffId(Long staffId);
    
    List<Appointment> findByStatus(AppointmentStatus status);
    
    @Query("SELECT a FROM Appointment a WHERE a.startTime >= :start AND a.endTime <= :end")
    List<Appointment> findByDateRange(
            @Param("start") LocalDateTime start, 
            @Param("end") LocalDateTime end);
    
    @Query("SELECT a FROM Appointment a WHERE a.staffId = :staffId " +
           "AND ((a.startTime BETWEEN :start AND :end) " +
           "OR (a.endTime BETWEEN :start AND :end))")
    List<Appointment> findOverlappingAppointmentsForStaff(
            @Param("staffId") Long staffId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
    
    @Query("SELECT a FROM Appointment a WHERE a.room.id = :roomId " +
           "AND ((a.startTime BETWEEN :start AND :end) " +
           "OR (a.endTime BETWEEN :start AND :end))")
    List<Appointment> findOverlappingAppointmentsForRoom(
            @Param("roomId") Long roomId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
    
    @Query("SELECT a FROM Appointment a WHERE " +
           "(a.startTime <= :end AND a.endTime >= :start)")
    List<Appointment> findOverlappingAppointmentsInDateRange(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
    
    List<Appointment> findByPatientIdAndStatus(Long patientId, AppointmentStatus status);
    
    List<Appointment> findByStaffIdAndStatus(Long staffId, AppointmentStatus status);
    
    @Query("SELECT a FROM Appointment a WHERE a.startTime >= :start " +
           "AND a.status NOT IN ('COMPLETED', 'CANCELLED', 'NO_SHOW')")
    List<Appointment> findUpcomingAppointments(@Param("start") LocalDateTime start);
} 