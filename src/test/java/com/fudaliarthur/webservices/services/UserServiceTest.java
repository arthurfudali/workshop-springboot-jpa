package com.fudaliarthur.webservices.services;

import com.fudaliarthur.webservices.entities.User;
import com.fudaliarthur.webservices.repositories.UserRepository;
import com.fudaliarthur.webservices.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user1;
    private User user2;


    @BeforeEach
    void setUp() {
        user1 = new User(1L, "Maria Brown", "maria@gmail.com", "988888888", "123456");
        user2 = new User(2L, "Alex Green", "alex@gmail.com", "977777777", "123456");
    }

    @Test
    @DisplayName("Should find all users ")
    void findAll() {
        // Arrange
        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.findAll();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(users);

        // verifica se o metodo foi chamado
        verify(userRepository, times(1)).findAll();


    }

    @Test
    @DisplayName("Should find user by ID when it exists")
    void findByIdUserFound() {
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.of(user1));

        User result = userService.findById(id);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(user1);
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void findByIdUserNotFound() {
        Long id = 99L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.findById(id);
        });

        verify(userRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should create user successfully")
    void createUser() {
        User newUser = new User(null, "Teste Da Silva", "teste@teste.com", "5599999999999", "12345");
        User savedUser = new User(3L, "Teste Da Silva", "teste@teste.com", "5599999999999", "12345");

        when(userRepository.save(newUser)).thenReturn(savedUser);

        User result = userService.createUser(newUser);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(savedUser);
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    @DisplayName("Should delete user when user exists")
    void deleteUser() {

        Long id = 1L;
        User mockUser = new User(id, "Maria Brown", "maria@gmail.com", "988888888", "123456");
        when(userRepository.findById(id)).thenReturn(Optional.of(mockUser));
        doNothing().when(userRepository).delete(mockUser);

        userService.deleteUser(id);

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).delete(mockUser);

    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent user")
    void deleteUserNotFound() {
        // Arrange
        Long id = 99L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.deleteUser(id);
        });

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, never()).delete(any());


    }

    @Test
    @DisplayName("Should update user successfully")
    void updateUser() {
        Long id = 1L;
        User existingUser = new User(1L, "Maria Brown", "maria@gmail.com", "988888888", "123456");
        User userDetails = new User(null, "Maria Updated", "maria.updated@gmail.com", "966666666", "123456");

        when(userRepository.getReferenceById(id)).thenReturn(existingUser);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateUser(id, userDetails);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Maria Updated");
        assertThat(result.getEmail()).isEqualTo("maria.updated@gmail.com");
        assertThat(result.getPhone()).isEqualTo("966666666");
        assertThat(result.getPassword()).isEqualTo("123456");

        verify(userRepository, times(1)).getReferenceById(id);
        verify(userRepository, times(1)).save(existingUser);


    }
}