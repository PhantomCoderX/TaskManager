package com.taskmanager.backend;

import com.taskmanager.backend.model.Status;
import com.taskmanager.backend.model.Task;
import com.taskmanager.backend.model.User;
import com.taskmanager.backend.repository.StatusRepository;
import com.taskmanager.backend.repository.TaskRepository;
import com.taskmanager.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private StatusRepository statusRepository;

    @Test
    void shouldSaveAndRetrieveTask() {
        // Сохраняем пользователя
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPasswordHash("hashed_password");
        user.setCreatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);

        // Сохраняем статус
        Status status = new Status();
        status.setName("Новая");
        status.setCreatedAt(LocalDateTime.now());
        Status savedStatus = statusRepository.save(status);

        // Создаем задачу
        Task task = new Task();
        task.setTitle("Тестовая задача");
        task.setContent("Описание тестовой задачи");
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        task.setUser(savedUser);
        task.setStatus(savedStatus);

        // Сохраняем и проверяем
        Task savedTask = taskRepository.save(task);
        Task foundTask = taskRepository.findById(savedTask.getId()).orElse(null);

        assertThat(foundTask).isNotNull();
        assertThat(foundTask.getTitle()).isEqualTo("Тестовая задача");
        assertThat(foundTask.getUser().getId()).isEqualTo(savedUser.getId());
        assertThat(foundTask.getStatus().getId()).isEqualTo(savedStatus.getId());
    }
}