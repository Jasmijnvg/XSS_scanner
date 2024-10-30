package com.jasmi.xss_scanner.dtos.user;

import com.jasmi.xss_scanner.dtos.role.RoleDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserOutputDto {
    private Long id;
    private String userName;
    private Set<RoleDto> roles;
    private boolean isEnabled;
}
