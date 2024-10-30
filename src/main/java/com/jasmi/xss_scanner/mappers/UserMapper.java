package com.jasmi.xss_scanner.mappers;

import com.jasmi.xss_scanner.dtos.user.UserInputDto;
import com.jasmi.xss_scanner.dtos.user.UserOutputDto;
import com.jasmi.xss_scanner.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper{
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;

    public UserMapper(RoleMapper roleMapper, PasswordEncoder passwordEncoder) {
        this.roleMapper = roleMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserOutputDto toUserOutputDto(User user) {
        UserOutputDto dto = new UserOutputDto();

        dto.setUserName(user.getUserName());
        dto.setEnabled(user.getEnabled());
        dto.setRoles(roleMapper.listRoleToOutput(user.getRoles()));

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
