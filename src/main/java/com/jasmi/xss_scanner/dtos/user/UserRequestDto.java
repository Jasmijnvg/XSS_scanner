package com.jasmi.xss_scanner.dtos.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
    @NotBlank(message = "Username is required")
    private String userName;
    @NotBlank(message = "password is required")
    private String password;
    @NotEmpty(message = "role is required")
    private String role;
}
