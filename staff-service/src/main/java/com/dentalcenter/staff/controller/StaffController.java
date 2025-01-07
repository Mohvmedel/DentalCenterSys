package com.dentalcenter.staff.controller;

import com.dentalcenter.staff.dto.StaffRequest;
import com.dentalcenter.staff.dto.StaffResponse;
import com.dentalcenter.staff.service.StaffService;
import com.dentalcenter.staff.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffController {
    private final StaffService staffService;
    private final StaffRepository staffRepository;

    @PostMapping
    public ResponseEntity<StaffResponse> createStaff(@RequestBody StaffRequest request) {
        return new ResponseEntity<>(staffService.createStaff(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<StaffResponse>> getAllStaff() {
        return ResponseEntity.ok(staffService.getAllStaff());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffResponse> getStaffById(@PathVariable Long id) {
        return ResponseEntity.ok(staffService.getStaffById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StaffResponse> updateStaff(@PathVariable Long id, @RequestBody StaffRequest request) {
        return ResponseEntity.ok(staffService.updateStaff(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<StaffResponse>> searchStaffByRole(@RequestParam String role) {
        return ResponseEntity.ok(staffService.getStaffByRole(role));
    }

    @GetMapping("/dentists")
    public ResponseEntity<List<StaffResponse>> getDentistsBySpecialization(
            @RequestParam(required = false) String specialization) {
        return ResponseEntity.ok(staffService.getDentistsBySpecialization(specialization));
    }

    @GetMapping("/exists/{id}")
    public boolean existsById(@PathVariable Long id) {
        return staffRepository.existsById(id);
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<StaffResponse> activateStaff(@PathVariable Long id) {
        return ResponseEntity.ok(staffService.activateStaff(id));
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<StaffResponse> deactivateStaff(@PathVariable Long id) {
        return ResponseEntity.ok(staffService.deactivateStaff(id));
    }

    @GetMapping("/{id}/active")
    public boolean isStaffActive(@PathVariable Long id) {
        return staffRepository.isStaffActive(id);
    }
} 