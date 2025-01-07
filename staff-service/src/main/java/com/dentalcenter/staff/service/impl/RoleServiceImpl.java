package com.dentalcenter.staff.service.impl;

import com.dentalcenter.staff.dto.RoleRequest;
import com.dentalcenter.staff.dto.RoleResponse;
import com.dentalcenter.staff.entity.Role;
import com.dentalcenter.staff.exception.ResourceNotFoundException;
import com.dentalcenter.staff.repository.RoleRepository;
import com.dentalcenter.staff.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public RoleResponse createRole(RoleRequest request) {
        if (roleRepository.existsByName(request.getName())) {
            throw new IllegalStateException("Role name already exists");
        }

        Role role = new Role();
        role.setName(request.getName().toUpperCase());
        role.setDescription(request.getDescription());
        
        Role savedRole = roleRepository.save(role);
        return mapToResponse(savedRole);
    }

    @Override
    public RoleResponse getRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
        return mapToResponse(role);
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RoleResponse updateRole(Long id, RoleRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
        
        if (!role.getName().equals(request.getName()) && 
            roleRepository.existsByName(request.getName())) {
            throw new IllegalStateException("Role name already exists");
        }
        
        role.setName(request.getName().toUpperCase());
        role.setDescription(request.getDescription());
        
        Role updatedRole = roleRepository.save(role);
        return mapToResponse(updatedRole);
    }

    @Override
    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Role not found with id: " + id);
        }
        roleRepository.deleteById(id);
    }

    private RoleResponse mapToResponse(Role role) {
        RoleResponse response = new RoleResponse();
        response.setId(role.getId());
        response.setName(role.getName());
        response.setDescription(role.getDescription());
        response.setCreatedAt(role.getCreatedAt());
        response.setUpdatedAt(role.getUpdatedAt());
        return response;
    }
} 