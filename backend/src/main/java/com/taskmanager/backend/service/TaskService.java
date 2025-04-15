package com.taskmanager.backend.service;

import com.taskmanager.backend.model.Task;
import com.taskmanager.backend.model.User;
import com.taskmanager.backend.model.Status;
import com.taskmanager.backend.repository.TaskRepository;
import com.taskmanager.backend.repository.UserRepository;
import com.taskmanager.backend.repository.StatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final StatusRepository statusRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, StatusRepository statusRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.statusRepository = statusRepository;
    }

    public Task createTask(String title, String content, Long userId, Long statusId) {
        Task task = new Task();
        task.setTitle(title);
        task.setContent(content);
        // Устанавливаем связь с пользователем и статусом
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Status> statusOpt = statusRepository.findById(statusId);
        if (userOpt.isPresent() && statusOpt.isPresent()) {
            task.setUser(userOpt.get());
            task.setStatus(statusOpt.get());
        } else {
            throw new RuntimeException("User or Status not found");
        }
        return taskRepository.save(task);
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task updateTask(Long id, String title, String content, Long userId, Long statusId) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if(optionalTask.isPresent()){
            Task task = optionalTask.get();
            task.setTitle(title);
            task.setContent(content);
            Optional<User> userOpt = userRepository.findById(userId);
            Optional<Status> statusOpt = statusRepository.findById(statusId);
            if(userOpt.isPresent() && statusOpt.isPresent()){
                task.setUser(userOpt.get());
                task.setStatus(statusOpt.get());
            } else {
                throw new RuntimeException("User or Status not found");
            }
            return taskRepository.save(task);
        }
        throw new RuntimeException("Task not found");
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
