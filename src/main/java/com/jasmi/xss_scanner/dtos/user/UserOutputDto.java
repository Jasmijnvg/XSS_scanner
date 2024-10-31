package com.jasmi.xss_scanner.dtos.user;

import com.jasmi.xss_scanner.dtos.role.RoleDto;
import com.jasmi.xss_scanner.dtos.scanrequest.ScanRequestOutputDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class UserOutputDto {
    private Long id;
    private String userName;
    private Set<RoleDto> roles;
    private boolean isEnabled;
    private List<ScanRequestOutputDto> scanRequests;
}
