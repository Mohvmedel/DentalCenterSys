package com.dentalcenter.staff.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RoleResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 