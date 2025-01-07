package com.dentalcenter.staff.service;

import com.dentalcenter.staff.dto.RoleRequest;
import com.dentalcenter.staff.dto.RoleResponse;
import java.util.List;

public interface RoleService {
    RoleResponse createRole(RoleRequest request);
    RoleResponse getRole(Long id);
    List<RoleResponse> getAllRoles();
    RoleResponse updateRole(Long id, RoleRequest request);
    void deleteRole(Long id);
} 