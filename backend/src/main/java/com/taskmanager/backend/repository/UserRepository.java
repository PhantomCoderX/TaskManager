package com.taskmanager.backend.repository;

import com.taskmanager.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // Добавлен метод поиска по email
}
