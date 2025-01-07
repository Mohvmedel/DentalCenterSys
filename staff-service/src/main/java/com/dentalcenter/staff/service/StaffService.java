package com.dentalcenter.staff.service;

import com.dentalcenter.staff.dto.StaffRequest;
import com.dentalcenter.staff.dto.StaffResponse;
import java.util.List;

public interface StaffService {
    StaffResponse createStaff(StaffRequest request);
    StaffResponse getStaffById(Long id);
    StaffResponse getStaffByEmail(String email);
    List<StaffResponse> getAllStaff();
    List<StaffResponse> getStaffByRole(String role);
    List<StaffResponse> getDentistsBySpecialization(String specialization);
    StaffResponse updateStaff(Long id, StaffRequest request);
    StaffResponse deactivateStaff(Long id);
    StaffResponse activateStaff(Long id);
    void deleteStaff(Long id);
} 