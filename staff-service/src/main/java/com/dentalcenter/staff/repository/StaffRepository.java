package com.dentalcenter.staff.repository;

import com.dentalcenter.staff.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    List<Staff> findByRoles_Name(String roleName);
    
    @Query("SELECT s FROM Staff s WHERE s.active = true")
    List<Staff> findAllActiveStaff();
    
    @Query("SELECT s FROM Staff s JOIN s.roles r WHERE s.active = true AND r.name = :roleName")
    List<Staff> findActiveStaffByRole(String roleName);
    
    @Query("SELECT s FROM Staff s WHERE s.active = true AND s.specialization = :specialization " +
           "AND EXISTS (SELECT r FROM s.roles r WHERE r.name = 'DENTIST')")
    List<Staff> findActiveDentistsBySpecialization(String specialization);
    
    @Query("SELECT s.active FROM Staff s WHERE s.id = :id")
    boolean isStaffActive(Long id);
} 