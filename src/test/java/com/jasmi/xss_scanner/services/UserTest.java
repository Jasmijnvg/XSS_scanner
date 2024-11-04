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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private Role role;
    private UserInputDto userInputDto;
    private UserOutputDto userOutputDto;
    private RoleDto roleDto;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setUserName("testuser");
        user.setEnabled(true);

        role = new Role();
        role.setId(1L);
        role.setRoleName("ROLE_USER");

        userInputDto = new UserInputDto();
        userInputDto.setUserName("testuser");
        userInputDto.setPassword("password");

        userOutputDto = new UserOutputDto();
        userOutputDto.setId(1L);
        userOutputDto.setUserName("testuser");

        roleDto = new RoleDto();
        roleDto.setRoleName("ROLE_USER");
    }

    @Test
    public void shouldCreateNewUser() {
        // Arrange
        when(userRepository.existsByUserName(userInputDto.getUserName())).thenReturn(false);
        when(roleRepository.findByRoleName("ROLE_USER")).thenReturn(Optional.of(role));
        when(userMapper.toUser(userInputDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserOutputDto(user)).thenReturn(userOutputDto);

        List<String> roleNames = new ArrayList<>();
        roleNames.add("ROLE_USER");

        // Act
        UserOutputDto result = userService.createNewUser(userInputDto, roleNames);

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUserName());
    }

    @Test
    public void shouldThrowExceptionWhenCreatingUserWithDuplicateUsername() {
        // Arrange
        when(userRepository.existsByUserName(userInputDto.getUserName())).thenReturn(true);

        List<String> roleNames = new ArrayList<>();
        roleNames.add("ROLE_USER");

        // Act & Assert
        assertThrows(DuplicateRecordException.class, () -> userService.createNewUser(userInputDto, roleNames));
    }

    @Test
    public void shouldUpdateUserRole() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByRoleName(roleDto.getRoleName())).thenReturn(Optional.of(role));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserOutputDto(user)).thenReturn(userOutputDto);

        // Act
        UserOutputDto result = userService.updateUserRole(1L, roleDto);

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUserName());
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingUserThatDoesntExist() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecordNotFoundException.class, () -> userService.updateUserRole(1L, roleDto));
    }

    @Test
    public void shouldGetUserByUserName() {
        // Arrange
        when(userRepository.findByUserName("testuser")).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.getUserByUserName("testuser");

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.get().getUserName());
    }

    @Test
    public void shouldGetAllUsers() {
        // Arrange
        List<User> users = List.of(user);
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toUserOutputDto(user)).thenReturn(userOutputDto);

        // Act
        List<UserOutputDto> result = userService.getAllUsers();

        // Assert
        assertEquals(1, result.size());
        assertEquals("testuser", result.getFirst().getUserName());
    }

    @Test
    public void shouldGetUserById() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toUserOutputDto(user)).thenReturn(userOutputDto);

        // Act
        UserOutputDto result = userService.getUserById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUserName());
    }

    @Test
    public void shouldThrowExceptionWhenUserNotFoundById() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecordNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    public void shouldLoadUserByUsername() {
        // Arrange
        when(userRepository.findByUserName("testuser")).thenReturn(Optional.of(user));

        // Act
        UserDetails result = userService.loadUserByUsername("testuser");

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    public void shouldThrowExceptionWhenLoadingUserByUsernameThatDoesntExist() {
        // Arrange
        when(userRepository.findByUserName("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("nonexistent"));
    }

    @Test
    public void shouldDeleteUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        // Act
        boolean result = userService.deleteUser(1L);

        // Assert
        assertTrue(result);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void shouldThrowExceptionWhenDeletingUserThatDoesntExist() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecordNotFoundException.class, () -> userService.deleteUser(1L));
    }
}
