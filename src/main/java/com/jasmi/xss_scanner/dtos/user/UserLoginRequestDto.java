package com.jasmi.xss_scanner.dtos.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequestDto {
    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 20, message = "Invalid input")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Invalid input")
    private String userName;

    @NotBlank(message = "Password is required")
    private String password;
}
