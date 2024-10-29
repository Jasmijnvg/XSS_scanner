package com.jasmi.xss_scanner.mappers;

import com.jasmi.xss_scanner.dtos.user.UserInputDto;
import com.jasmi.xss_scanner.dtos.user.UserOutputDto;
import com.jasmi.xss_scanner.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper{

    public static UserOutputDto toUserOutputDto(User user) {
        UserOutputDto dto = new UserOutputDto();

        dto.setUserName(user.getUserName());

        return dto;
    }

    public static User toUser(UserInputDto userInputDto) {
        User user = new User();

        user.setUserName(userInputDto.getUserName());
        user.setPassword(userInputDto.getPassword());

        return user;
    }
}
