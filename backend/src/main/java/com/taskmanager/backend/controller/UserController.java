package com.taskmanager.backend.controller;

import com.taskmanager.backend.dto.CreateUserDTO;
import com.taskmanager.backend.dto.UserDTO;
import com.taskmanager.backend.model.User;
import com.taskmanager.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Преобразование модели User в DTO
    private UserDTO convertToDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPasswordHash(user.getPasswordHash());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody CreateUserDTO createUserDTO) {
        User user = userService.registerUser(
                createUserDTO.getUsername(),
                createUserDTO.getEmail(),
                createUserDTO.getPasswordHash()
        );
        return ResponseEntity.ok(convertToDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(convertToDto(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody CreateUserDTO createUserDTO) {
        User updatedUser = userService.updateUser(
                id,
                createUserDTO.getUsername(),
                createUserDTO.getEmail(),
                createUserDTO.getPasswordHash()
        );
        if (updatedUser != null) {
            return ResponseEntity.ok(convertToDto(updatedUser));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
