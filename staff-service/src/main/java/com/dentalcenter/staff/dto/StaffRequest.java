package com.dentalcenter.staff.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Set;

@Data
public class StaffRequest {
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    private String phoneNumber;
    
    @NotEmpty(message = "At least one role is required")
    private Set<Long> roleIds;
    
    private String specialization;
    private String licenseNumber;
    private String qualification;
    private String schedule;
    private String notes;
} 