package org.example.springmvc_jsonview.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springmvc_jsonview.controller.UserController;
import org.example.springmvc_jsonview.model.User;
import org.example.springmvc_jsonview.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /v.1.0.0/api/users/all - Should return list of users")
    void testGetAllUsers() throws Exception {
        User user1 = new User();
        user1.setName("Alice");
        user1.setEmail("alice@example.com");

        User user2 = new User();
        user2.setName("Bob");
        user2.setEmail("bob@example.com");

        when(userService.findAllUsers()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/v.1.0.0/api/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("GET /v.1.0.0/api/users/{id} - Should return user")
    void testGetUserById() throws Exception {
        User user = new User();
        user.setName("Charlie");
        user.setEmail("charlie@example.com");

        when(userService.findUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/v.1.0.0/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Charlie"))
                .andExpect(jsonPath("$.email").value("charlie@example.com"));
    }

    @Test
    @DisplayName("POST /v.1.0.0/api/users - Should create user")
    void testCreateUser() throws Exception {
        User user = new User();
        user.setName("Diana");
        user.setEmail("diana@example.com");

        when(userService.saveUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/v.1.0.0/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Diana"))
                .andExpect(jsonPath("$.email").value("diana@example.com"));
    }

    @Test
    @DisplayName("PUT /v.1.0.0/api/users/{id} - Should update user")
    void testUpdateUser() throws Exception {
        User existingUser = new User();
        existingUser.setName("OldName");
        existingUser.setEmail("old@example.com");

        User updatedUser = new User();
        updatedUser.setName("NewName");
        updatedUser.setEmail("new@example.com");

        when(userService.findUserById(1L)).thenReturn(existingUser);
        when(userService.saveUser(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/v.1.0.0/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("NewName"))
                .andExpect(jsonPath("$.email").value("new@example.com"));
    }

    @Test
    @DisplayName("DELETE /v.1.0.0/api/users/{id} - Should delete user")
    void testDeleteUser() throws Exception {
        User user = new User();
        user.setName("ToDelete");
        user.setEmail("delete@example.com");

        when(userService.findUserById(1L)).thenReturn(user);
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/v.1.0.0/api/users/1"))
                .andExpect(status().isNoContent());
    }
}
