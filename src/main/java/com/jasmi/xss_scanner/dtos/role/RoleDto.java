package com.jasmi.xss_scanner.dtos.role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDto {
    private String roleName;
    private boolean active;
    private String description;

    public RoleDto(){
    }
}
