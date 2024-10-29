package com.jasmi.xss_scanner.controllers;

import com.jasmi.xss_scanner.dtos.user.UserInputDto;
import com.jasmi.xss_scanner.dtos.user.UserOutputDto;
import com.jasmi.xss_scanner.mappers.UserDtoMapper;
import com.jasmi.xss_scanner.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/xss_scanner_api")
public class UserController {
    private final UserService userService;
    private final UserDtoMapper userDtoMapper;

    public UserController(UserService userService, UserDtoMapper userDtoMapper) {
        this.userService = userService;
        this.userDtoMapper = userDtoMapper;
    }


    @PostMapping("/user/register")
    public ResponseEntity<String> registerUser(@RequestBody UserInputDto userInputDto) {
        userService.registerUser(userInputDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registration successful");
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserOutputDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(UserService.getUserById);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserOutputDto>> getUsers() {
        return ResponseEntity.ok(UserService.getUsers);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<UserOutputDto> updateUser(@PathVariable Long id, @RequestBody UserInputDto user) {
        userService.updateUser(id, user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
