package com.jasmi.xss_scanner.services;

import com.jasmi.xss_scanner.dtos.user.UserInputDto;
import com.jasmi.xss_scanner.dtos.user.UserOutputDto;
import com.jasmi.xss_scanner.exceptions.RecordNotFoundException;
import com.jasmi.xss_scanner.mappers.UserMapper;
import com.jasmi.xss_scanner.models.User;
import com.jasmi.xss_scanner.models.Role;
import com.jasmi.xss_scanner.repositories.UserRepository;
import com.jasmi.xss_scanner.repositories.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(UserInputDto userInputDto) {
        User user = UserMapper.toUser(userInputDto);

        Role defaultRole = roleRepository.findByRoleName("User")
                .orElseThrow(() -> new RuntimeException("Default role 'User' not found"));
        user.setRole(defaultRole);
        user.setPassword(passwordEncoder.encode(userInputDto.getPassword()));

        userRepository.save(user);
    }

    public boolean authenticate(String userName, String password) {
        Optional<User> userOptional = userRepository.findByUserName(userName);
        return userOptional.isPresent() &&
                passwordEncoder.matches(password, userOptional.get().getPassword());
    }

    public List<UserOutputDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map((x)->userMapper.toUserOutputDto(x))
                .collect(Collectors.toList());
    }

    public UserOutputDto getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User u = optionalUser.get();
            return userMapper.toUserOutputDto(u);
        }
        else {
            throw new RecordNotFoundException("Vulnerability " + id + " not found");
        }
    }

    public UserOutputDto updateUser(Long id, UserInputDto userInputDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUserName(userInputDTO.getUserName());
        user.setPassword(passwordEncoder.encode(userInputDTO.getPassword()));

        userRepository.save(user);
        return UserMapper.toUserOutputDto(user);
    }

    public boolean deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);
        return true;
    }

}
