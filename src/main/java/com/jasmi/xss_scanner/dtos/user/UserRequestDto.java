package com.jasmi.xss_scanner.dtos.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
    @NotBlank(message = "Username is required")
    private String userName;
    @NotBlank(message = "password is required")
    private String password;
    private String[] roles;
}
