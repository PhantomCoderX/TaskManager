package com.taskmanager.backend.service;

import com.taskmanager.backend.model.Status;
import com.taskmanager.backend.repository.StatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatusService {

    private final StatusRepository statusRepository;

    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public Status createStatus(String name) {
        Status status = new Status();
        status.setName(name);
        return statusRepository.save(status);
    }

    public Optional<Status> getStatusById(Long id) {
        return statusRepository.findById(id);
    }

    public List<Status> getAllStatuses() {
        return statusRepository.findAll();
    }

    public Status updateStatus(Long id, String name) {
        Optional<Status> optionalStatus = statusRepository.findById(id);
        if(optionalStatus.isPresent()){
            Status status = optionalStatus.get();
            status.setName(name);
            return statusRepository.save(status);
        }
        throw new RuntimeException("Status not found");
    }

    public void deleteStatus(Long id) {
        statusRepository.deleteById(id);
    }
}
