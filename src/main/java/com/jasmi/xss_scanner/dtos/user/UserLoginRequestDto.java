package com.jasmi.xss_scanner.dtos.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequestDto {
    private String userName;
    private String password;
}
