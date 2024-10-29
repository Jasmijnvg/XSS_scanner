package com.jasmi.xss_scanner.mappers;
import com.jasmi.xss_scanner.dtos.user.UserChangePasswordRequestDto;
import com.jasmi.xss_scanner.dtos.user.UserInputDto;
import com.jasmi.xss_scanner.dtos.user.UserRequestDto;
import com.jasmi.xss_scanner.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserDtoMapper {
    public User mapToUser(UserRequestDto userDto) {
        var dto = new User();

        dto.setUserName(userDto.getUserName());
        dto.setPassword(userDto.getPassword());

        return dto;
    }

//    public User mapToUser(UserChangePasswordRequestDto userDto, Long id) { //Long id toevoegen?
//        var result = new User();
//
//        result.setPassword(userDto.getPassword());
//
//        return result;
//    }
}
