package com.jasmi.xss_scanner.mappers;

import com.jasmi.xss_scanner.dtos.scanrequest.ScanRequestOutputDto;
import com.jasmi.xss_scanner.dtos.user.UserInputDto;
import com.jasmi.xss_scanner.dtos.user.UserOutputDto;
import com.jasmi.xss_scanner.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper{
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;
    private final ScanRequestMapper scanRequestMapper;

    public UserMapper(RoleMapper roleMapper, PasswordEncoder passwordEncoder, ScanRequestMapper scanRequestMapper) {
        this.roleMapper = roleMapper;
        this.passwordEncoder = passwordEncoder;
        this.scanRequestMapper = scanRequestMapper;
    }

    public UserOutputDto toUserOutputDto(User user) {
        var dto = new UserOutputDto();

        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setEnabled(user.getEnabled());
        dto.setRoles(roleMapper.listRoleToOutput(user.getRoles()));
        if (user.getScanRequests() != null) {
            dto.setScanRequests(user.getScanRequests()
                    .stream()
                    .map(scanRequestMapper::toScanRequestDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public User toUser(UserInputDto userInputDto) {
        User user = new User();

        user.setUserName(userInputDto.getUserName());
        user.setPassword(passwordEncoder.encode(userInputDto.getPassword()));
        user.setRoles(roleMapper.listInputToRoles(userInputDto.getRoles()));

        return user;
    }
}
