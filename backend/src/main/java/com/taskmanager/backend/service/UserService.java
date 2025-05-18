package com.taskmanager.backend.service;

import com.taskmanager.backend.model.User;
import com.taskmanager.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String username, String email, String rawPassword) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already in use");
        }
        User user = new User(email, passwordEncoder.encode(rawPassword));
        user.setUsername(username);
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long id,
                           String username,
                           String email,
                           String rawPassword) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUsername(username);
            user.setEmail(email);
            if (rawPassword != null && !rawPassword.isEmpty()) {
                user.setPasswordHash(passwordEncoder.encode(rawPassword));
            }
            return userRepository.save(user);
        }
        return null;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public boolean authenticate(String email, String rawPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional
                .map(u -> passwordEncoder.matches(rawPassword, u.getPasswordHash()))
                .orElse(false);
    }

    public void changePassword(String email,
                               String oldRawPassword,
                               String newRawPassword) {
        User u = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(oldRawPassword, u.getPasswordHash())) {
            throw new RuntimeException("Old password is incorrect");
        }
        u.setPasswordHash(passwordEncoder.encode(newRawPassword));
        userRepository.save(u);
    }
}
