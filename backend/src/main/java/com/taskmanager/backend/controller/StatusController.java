package com.taskmanager.backend.controller;

import com.taskmanager.backend.dto.CreateStatusDTO;
import com.taskmanager.backend.dto.StatusDTO;
import com.taskmanager.backend.model.Status;
import com.taskmanager.backend.service.StatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/statuses")
public class StatusController {

    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    private StatusDTO convertToDto(Status status) {
        StatusDTO dto = new StatusDTO();
        dto.setId(status.getId());
        dto.setName(status.getName());
        dto.setCreatedAt(status.getCreatedAt());
        return dto;
    }

    @PostMapping
    public ResponseEntity<StatusDTO> createStatus(@RequestBody CreateStatusDTO createStatusDTO) {
        Status status = statusService.createStatus(createStatusDTO.getName());
        return ResponseEntity.ok(convertToDto(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatusDTO> getStatus(@PathVariable Long id) {
        return statusService.getStatusById(id)
                .map(status -> ResponseEntity.ok(convertToDto(status)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<StatusDTO>> getAllStatuses() {
        List<StatusDTO> statuses = statusService.getAllStatuses().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(statuses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StatusDTO> updateStatus(@PathVariable Long id, @RequestBody CreateStatusDTO createStatusDTO) {
        Status updatedStatus = statusService.updateStatus(id, createStatusDTO.getName());
        return ResponseEntity.ok(convertToDto(updatedStatus));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatus(@PathVariable Long id) {
        statusService.deleteStatus(id);
        return ResponseEntity.noContent().build();
    }
}
