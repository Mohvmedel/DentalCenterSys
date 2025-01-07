package com.dentalcenter.appointment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "staff-service")
public interface StaffClient {
    @GetMapping("/api/staff/exists/{id}")
    boolean existsById(@PathVariable Long id);

    @GetMapping("/api/staff/{id}/active")
    boolean isStaffActive(@PathVariable Long id);
} 