package com.taskmanager.backend.repository;

import com.taskmanager.backend.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {
}