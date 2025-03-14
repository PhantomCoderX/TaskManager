package com.taskmanager.backend.repository;

import com.taskmanager.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}