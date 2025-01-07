package com.dentalcenter.staff.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class StaffResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private boolean active;
    private Set<RoleResponse> roles;
    private String specialization;
    private String licenseNumber;
    private String qualification;
    private String schedule;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 