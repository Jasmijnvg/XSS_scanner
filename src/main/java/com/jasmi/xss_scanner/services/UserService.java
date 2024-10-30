package com.jasmi.xss_scanner.services;

import com.jasmi.xss_scanner.dtos.user.UserInputDto;
import com.jasmi.xss_scanner.dtos.user.UserOutputDto;
import com.jasmi.xss_scanner.exceptions.RecordNotFoundException;
import com.jasmi.xss_scanner.mappers.UserMapper;
import com.jasmi.xss_scanner.models.User;
import com.jasmi.xss_scanner.models.Role;
import com.jasmi.xss_scanner.repositories.UserRepository;
import com.jasmi.xss_scanner.repositories.RoleRepository;
import com.jasmi.xss_scanner.security.ApiUserDetails;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
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

//    public UserOutputDto createNewUser(UserInputDto newUser, List<String> roles) {
//        List<Role> validRoles = new ArrayList<>();
//
//        for (String roleName : roles) {
//            Optional<Role> optionalRole = roleRepository.findByRoleName(roleName);
//            optionalRole.ifPresent(validRoles::add);
//        }
//
//        var user = userMapper.toUser(newUser);
//
//        for (Role role : validRoles) {
//            user.getRoles().add(role);
//        }
//
//        updateRoleUser(user);
//        var savedUser = userRepository.save(user);
//        newUser.setId(savedUser.getId());
//
//        return userMapper.toUserOutputDto(savedUser);
//    }

    public UserOutputDto createNewUser(UserInputDto newUser, List<String> roleNames) {
        List<Role> roles = new ArrayList<>();
        for (String roleName : roleNames) {
            Role role = roleRepository.findByRoleName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
            roles.add(role);
        }

        User user = userMapper.toUser(newUser);
        user.setRoles(roles);
        user.setEnabled(true);

        User savedUser = userRepository.save(user);
        return userMapper.toUserOutputDto(savedUser);
    }


    private void updateRoleUser(User user) {
        for (Role role : user.getRoles()) {
            role.getUsers().add(user);
        }
    }

    @Transactional
    public UserOutputDto createNewUser(UserInputDto newUser, String[] roles) {
        return createNewUser(newUser, Arrays.asList(roles));
    }

    public Optional<User> getUserByUserName(String username) {
        var user = userRepository.findByUserName(username);
        return getOptionalUserModel(user);
    }

    private Optional<User> getOptionalUserModel(Optional<User> user) {
        return user;
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

//    public UserOutputDto updateUser(Long id, UserInputDto userInputDTO) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        user.setUserName(userInputDTO.getUserName());
//        user.setPassword(passwordEncoder.encode(userInputDTO.getPassword()));
//
//        userRepository.save(user);
//        return UserMapper.toUserOutputDto(user);
//    }


//    public Optional<User> getUserByUserNameAndPassword(String username, String password) {
//        return userRepository.findByUserNameAndPassword(username, password);
//    }
//
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
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);
        return true;
    }
}
