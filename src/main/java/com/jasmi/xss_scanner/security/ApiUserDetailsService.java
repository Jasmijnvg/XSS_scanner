package com.jasmi.xss_scanner.security;

import com.jasmi.xss_scanner.dtos.user.UserInputDto;
import com.jasmi.xss_scanner.exceptions.RecordNotFoundException;
import com.jasmi.xss_scanner.mappers.RoleMapper;
import com.jasmi.xss_scanner.mappers.UserMapper;
import com.jasmi.xss_scanner.models.Role;
import com.jasmi.xss_scanner.models.User;
import com.jasmi.xss_scanner.repositories.RoleRepository;
import com.jasmi.xss_scanner.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApiUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    public ApiUserDetailsService(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper, RoleMapper roleMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;

        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
    }

    @Transactional
    public boolean createUser(UserInputDto userInputDto, String roleName) {
        Role role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new RecordNotFoundException("Role "+roleName+" not found"));

        User user = userMapper.toUser(userInputDto);

        user.setRole(role);

        User savedUser = userRepository.save(user);

        updateRoleUser(savedUser);

        return savedUser != null;
    }

    private void updateRoleUser(User user) {
        Role role = user.getRole();
        if (role != null) {
            role.getUsers().add(user);
        }
    }

    public Optional<User> getUserByUserName(String username) {
        return userRepository.findByUserName(username);
    }

    public Optional<User> getUserByUserNameAndPassword(String username, String password) {
        return userRepository.findByUserNameAndPassword(username, password);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = getUserByUserName(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        return new ApiUserDetails(user.get());
    }
}