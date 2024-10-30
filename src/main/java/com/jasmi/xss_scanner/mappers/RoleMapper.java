package com.jasmi.xss_scanner.mappers;

import com.jasmi.xss_scanner.dtos.role.RoleDto;
import com.jasmi.xss_scanner.models.Role;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoleMapper {
    public RoleDto toRoleOutputDto (Role role) {
        if (role == null) {
            return null;
        }
        RoleDto dto = new RoleDto();

        dto.setRoleName(role.getRoleName());
        dto.setDescription(role.getDescription());
        dto.setActive(role.isActive());

        return dto;
    }

    public Role toRole(RoleDto dto){
        if (dto == null) {
            return null;
        }

        Role role = new Role();

        role.setRoleName(dto.getRoleName());
        role.setDescription(dto.getDescription());
        role.setActive(dto.isActive());

        return role;
    }

    public Set<RoleDto> listRoleToOutput(List<Role> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream()
                .map(this::toRoleOutputDto)
                .collect(Collectors.toSet());
    }

    public List<Role> listInputToRoles(List<RoleDto> dtos) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
                .map(this::toRole)
                .collect(Collectors.toList());
    }

}
