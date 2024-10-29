package com.jasmi.xss_scanner.dtos.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInputDto {
    private String userName;
    private String password;
}
