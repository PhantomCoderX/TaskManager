package com.taskmanager.backend.controller;

import com.taskmanager.backend.dto.CreateTaskDTO;
import com.taskmanager.backend.dto.TaskDTO;
import com.taskmanager.backend.model.Task;
import com.taskmanager.backend.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    private TaskDTO convertToDto(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setContent(task.getContent());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());
        dto.setUserId(task.getUser().getId());
        dto.setStatusId(task.getStatus().getId());
        return dto;
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody CreateTaskDTO createTaskDTO) {
        Task task = taskService.createTask(
                createTaskDTO.getTitle(),
                createTaskDTO.getContent(),
                createTaskDTO.getUserId(),
                createTaskDTO.getStatusId()
        );
        return ResponseEntity.ok(convertToDto(task));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(task -> ResponseEntity.ok(convertToDto(task)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<TaskDTO> tasks = taskService.getAllTasks().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody CreateTaskDTO createTaskDTO) {
        Task updatedTask = taskService.updateTask(
                id,
                createTaskDTO.getTitle(),
                createTaskDTO.getContent(),
                createTaskDTO.getUserId(),
                createTaskDTO.getStatusId()
        );
        return ResponseEntity.ok(convertToDto(updatedTask));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
