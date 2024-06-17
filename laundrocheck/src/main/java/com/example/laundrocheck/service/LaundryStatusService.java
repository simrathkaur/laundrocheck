package com.example.laundrocheck.service;

import com.example.laundrocheck.model.LaundryStatus;
import com.example.laundrocheck.repository.LaundryStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LaundryStatusService {

    @Autowired
    private LaundryStatusRepository repository;

    public List<LaundryStatus> getAllStatuses() {
        return repository.findAll();
    }

    public LaundryStatus updateStatus(Long id, String action) {
        LaundryStatus status = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid status ID"));

        switch (action) {
            case "inUse":
                status.setAvailable(false);
                status.setInUse(true);
                status.setDone(false);
                break;
            case "done":
                status.setAvailable(false);
                status.setInUse(false);
                status.setDone(true);
                break;
            case "available":
                status.setAvailable(true);
                status.setInUse(false);
                status.setDone(false);
                break;
            default:
                throw new IllegalArgumentException("Invalid action");
        }

        return repository.save(status);
    }
}
