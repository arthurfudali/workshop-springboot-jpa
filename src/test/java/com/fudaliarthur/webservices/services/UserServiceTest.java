package com.fudaliarthur.webservices.services;

import com.fudaliarthur.webservices.entities.User;
import com.fudaliarthur.webservices.repositories.UserRepository;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Autowired
    @InjectMocks
    private UserService userService;


    @Test
    @DisplayName("Should find all users ")
    void findAll() {
        User user1 = new User(1L, "Maria Brown", "maria@gmail.com", "988888888", "123456");
        User user2 = new User(2L, "Alex Green", "alex@gmail.com", "977777777", "123456");

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
    void findById() {
    }

    @Test
    void createUser() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void updateUser() {
    }
}