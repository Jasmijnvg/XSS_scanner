package com.jasmi.xss_scanner.dtos.user;

import com.jasmi.xss_scanner.dtos.role.RoleDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserInputDto {
    private Long id;
    @NotEmpty(message = "Username may not be empty")
    private String userName;
    @NotEmpty(message = "Password may not be empty")
    @Size(min = 8, message = "Password needs at least 8 characters")
    private String password;
    private List<RoleDto> roles = new ArrayList<>();
    private boolean isEnabled;
}
