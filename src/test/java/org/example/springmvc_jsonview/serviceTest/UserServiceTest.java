package org.example.springmvc_jsonview.serviceTest;

import org.example.springmvc_jsonview.exception.ResourceNotFoundException;
import org.example.springmvc_jsonview.model.User;
import org.example.springmvc_jsonview.repository.UserRepository;
import org.example.springmvc_jsonview.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testFindAllUsers() {
        List<User> users = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAllUsers();

        assertEquals(2, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void testFindUserById_UserExists() {
        User user = new User();
        user.setName("Test");
        user.setEmail("test@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findUserById(1L);

        assertEquals(user, result); // сравниваем, что вернулся тот же объект
        verify(userRepository).findById(1L);
    }

    @Test
    void testFindUserById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.findUserById(1L);
        });

        assertEquals("Пользователь с id 1 не найден", exception.getMessage());
    }

    @Test
    void testSaveUser() {
        User user = new User();
        user.setName("Test");
        user.setEmail("test@example.com");

        when(userRepository.save(user)).thenReturn(user);

        User saved = userService.saveUser(user);

        assertEquals("Test", saved.getName());
        assertEquals("test@example.com", saved.getEmail());
        verify(userRepository).save(user);
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }
}