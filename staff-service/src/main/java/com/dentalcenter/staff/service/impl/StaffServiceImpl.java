package com.dentalcenter.staff.service.impl;

import com.dentalcenter.staff.dto.RoleResponse;
import com.dentalcenter.staff.dto.StaffRequest;
import com.dentalcenter.staff.dto.StaffResponse;
import com.dentalcenter.staff.entity.Role;
import com.dentalcenter.staff.entity.Staff;
import com.dentalcenter.staff.exception.ResourceNotFoundException;
import com.dentalcenter.staff.repository.RoleRepository;
import com.dentalcenter.staff.repository.StaffRepository;
import com.dentalcenter.staff.service.StaffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final RoleRepository roleRepository;

    @Override
    public StaffResponse createStaff(StaffRequest request) {
        if (staffRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Email already registered");
        }

        Staff staff = new Staff();
        mapRequestToStaff(request, staff);
        staff.setRoles(getRolesFromIds(request.getRoleIds()));
        
        Staff savedStaff = staffRepository.save(staff);
        return mapToResponse(savedStaff);
    }

    @Override
    public StaffResponse getStaffById(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + id));
        return mapToResponse(staff);
    }

    @Override
    public StaffResponse getStaffByEmail(String email) {
        Staff staff = staffRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with email: " + email));
        return mapToResponse(staff);
    }

    @Override
    public List<StaffResponse> getAllStaff() {
        return staffRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StaffResponse> getStaffByRole(String role) {
        return staffRepository.findByRoles_Name(role).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StaffResponse> getDentistsBySpecialization(String specialization) {
        return staffRepository.findActiveDentistsBySpecialization(specialization).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public StaffResponse updateStaff(Long id, StaffRequest request) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + id));

        if (!staff.getEmail().equals(request.getEmail()) && 
            staffRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Email already registered");
        }

        mapRequestToStaff(request, staff);
        staff.setRoles(getRolesFromIds(request.getRoleIds()));
        
        Staff updatedStaff = staffRepository.save(staff);
        return mapToResponse(updatedStaff);
    }

    @Override
    public StaffResponse deactivateStaff(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + id));
        staff.setActive(false);
        return mapToResponse(staffRepository.save(staff));
    }

    @Override
    public StaffResponse activateStaff(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + id));
        staff.setActive(true);
        return mapToResponse(staffRepository.save(staff));
    }

    @Override
    public void deleteStaff(Long id) {
        if (!staffRepository.existsById(id)) {
            throw new ResourceNotFoundException("Staff not found with id: " + id);
        }
        staffRepository.deleteById(id);
    }

    private Set<Role> getRolesFromIds(Set<Long> roleIds) {
        Set<Role> roles = new HashSet<>();
        for (Long roleId : roleIds) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));
            roles.add(role);
        }
        return roles;
    }

    private void mapRequestToStaff(StaffRequest request, Staff staff) {
        staff.setFirstName(request.getFirstName());
        staff.setLastName(request.getLastName());
        staff.setEmail(request.getEmail());
        staff.setPhoneNumber(request.getPhoneNumber());
        staff.setSpecialization(request.getSpecialization());
        staff.setLicenseNumber(request.getLicenseNumber());
        staff.setQualification(request.getQualification());
        staff.setSchedule(request.getSchedule());
        staff.setNotes(request.getNotes());
    }

    private StaffResponse mapToResponse(Staff staff) {
        StaffResponse response = new StaffResponse();
        response.setId(staff.getId());
        response.setFirstName(staff.getFirstName());
        response.setLastName(staff.getLastName());
        response.setEmail(staff.getEmail());
        response.setPhoneNumber(staff.getPhoneNumber());
        response.setActive(staff.isActive());
        response.setSpecialization(staff.getSpecialization());
        response.setLicenseNumber(staff.getLicenseNumber());
        response.setQualification(staff.getQualification());
        response.setSchedule(staff.getSchedule());
        response.setNotes(staff.getNotes());
        response.setCreatedAt(staff.getCreatedAt());
        response.setUpdatedAt(staff.getUpdatedAt());

        Set<RoleResponse> roleResponses = staff.getRoles().stream()
                .map(role -> {
                    RoleResponse roleResponse = new RoleResponse();
                    roleResponse.setId(role.getId());
                    roleResponse.setName(role.getName());
                    roleResponse.setDescription(role.getDescription());
                    return roleResponse;
                })
                .collect(Collectors.toSet());
        response.setRoles(roleResponses);

        return response;
    }
} 