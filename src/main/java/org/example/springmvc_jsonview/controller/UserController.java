package org.example.springmvc_jsonview.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import org.example.springmvc_jsonview.dto.Views;
import org.example.springmvc_jsonview.model.User;
import org.example.springmvc_jsonview.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v.1.0.0/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    @JsonView(Views.UserSummary.class)
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    @JsonView(Views.UserDetails.class)
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findUserById(id); // если не найден — выбросит исключение
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.saveUser(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails) {
        User user = userService.findUserById(id); // если не найден — выбросит исключение
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        User updatedUser = userService.saveUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.findUserById(id); // проверяем, что пользователь существует — если нет, выбросит исключение
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
