package com.dentalcenter.appointment.repository;

import com.dentalcenter.appointment.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    
    Optional<Room> findByRoomNumber(String roomNumber);
    
    List<Room> findByType(String type);
    
    List<Room> findByActiveTrue();
    
    @Query("SELECT r FROM Room r WHERE r.active = true AND r.id NOT IN " +
           "(SELECT DISTINCT a.room.id FROM Appointment a WHERE " +
           "(a.startTime <= :end AND a.endTime >= :start))")
    List<Room> findAvailableRooms(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
    
    boolean existsByRoomNumber(String roomNumber);
    
    boolean existsByRoomNumberAndIdNot(String roomNumber, Long id);
} 