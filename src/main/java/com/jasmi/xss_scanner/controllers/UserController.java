package com.jasmi.xss_scanner.controllers;

import com.jasmi.xss_scanner.dtos.user.UserInputDto;
import com.jasmi.xss_scanner.dtos.user.UserOutputDto;
import com.jasmi.xss_scanner.mappers.UserDtoMapper;
import com.jasmi.xss_scanner.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/xss_scanner_api")
public class UserController {
    private final UserService userService;
    private final UserDtoMapper userDtoMapper;
    private final HttpServletRequest httpServletRequest;

    public UserController(UserService userService, UserDtoMapper userDtoMapper, HttpServletRequest httpServletRequest) {
        this.userService = userService;
        this.userDtoMapper = userDtoMapper;
        this.httpServletRequest = httpServletRequest;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserOutputDto> createUser(@RequestBody @Valid UserInputDto newUser) {
        UserOutputDto createdUser = userService.createNewUser(newUser, List.of("ROLE_USER"));
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdUser.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdUser);
    }

//    @PostMapping("/user/register")
//    public ResponseEntity<String> registerUser(@RequestBody UserInputDto userInputDto) {
//        userService.registerUser(userInputDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body("User registration successful");
//    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserOutputDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserOutputDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

//    @PutMapping("/user/{id}")
//    public ResponseEntity<UserOutputDto> updateUser(@PathVariable Long id, @RequestBody UserInputDto user) {
//        userService.updateUser(id, user);
//        return ResponseEntity.noContent().build();
//    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
