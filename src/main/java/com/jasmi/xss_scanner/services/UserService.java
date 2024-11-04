package com.jasmi.xss_scanner.services;

import com.jasmi.xss_scanner.dtos.role.RoleDto;
import com.jasmi.xss_scanner.dtos.user.UserInputDto;
import com.jasmi.xss_scanner.dtos.user.UserOutputDto;
import com.jasmi.xss_scanner.exceptions.DuplicateRecordException;
import com.jasmi.xss_scanner.exceptions.RecordNotFoundException;
import com.jasmi.xss_scanner.mappers.UserMapper;
import com.jasmi.xss_scanner.models.User;
import com.jasmi.xss_scanner.models.Role;
import com.jasmi.xss_scanner.repositories.UserRepository;
import com.jasmi.xss_scanner.repositories.RoleRepository;
import com.jasmi.xss_scanner.security.ApiUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

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

    public UserOutputDto createNewUser(UserInputDto newUser, List<String> roleNames) {
        if (userRepository.existsByUserName(newUser.getUserName())){
            throw new DuplicateRecordException("This username already exists");
        }
        List<Role> roles = new ArrayList<>();
        for (String roleName : roleNames) {
            Role role = roleRepository.findByRoleName(roleName)
                    .orElseThrow(() -> new RecordNotFoundException("Role not found: " + roleName));
            roles.add(role);
        }

        User user = userMapper.toUser(newUser);
        user.setRoles(roles);
        user.setEnabled(true);

        User savedUser = userRepository.save(user);
        return userMapper.toUserOutputDto(savedUser);
    }

    public UserOutputDto updateUserRole(Long id, RoleDto roleDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        Role role = roleRepository.findByRoleName(roleDto.getRoleName())
                .orElseThrow(() -> new RecordNotFoundException("Role not found"));

        user.getRoles().clear();
        user.getRoles().add(role);

        User updatedUser = userRepository.save(user);

        return userMapper.toUserOutputDto(updatedUser);
    }

    public Optional<User> getUserByUserName(String username) {
        var user = userRepository.findByUserName(username);
        return user;
    }
//
//    private Optional<User> getOptionalUserModel(Optional<User> user) {
//        return user;
//    }

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
            throw new RecordNotFoundException("User "+id+" not found");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = getUserByUserName(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        return new ApiUserDetails(user.get());
    }

    public boolean deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("User "+id+" not found"));

        user.getRoles().clear();

        userRepository.save(user);

        userRepository.delete(user);
        return true;
    }

}
