package com.example.laundrocheck.controller;

import com.example.laundrocheck.model.LaundryStatus;
import com.example.laundrocheck.repository.LaundryStatusRepository;
import com.example.laundrocheck.service.LaundryStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/status")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class LaundryStatusController {

    private final LaundryStatusRepository laundryStatusRepository;

    @Autowired
    public LaundryStatusController(LaundryStatusRepository laundryStatusRepository) {
        this.laundryStatusRepository = laundryStatusRepository;
    }

    @GetMapping("/machine-status")
    public List<LaundryStatus> getAllMachineStatus() {
        return laundryStatusRepository.findAll();
    }

    @PutMapping("/machine/{id}/{action}")
    public LaundryStatus updateMachineStatus(@PathVariable Long id, @PathVariable String action, @RequestBody(required = false) String userEmail) {
        LaundryStatus machine = laundryStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Machine not found"));

        switch (action) {
            case "start":
                if (machine.getAvailable()) {
                    machine.setAvailable(false);
                    machine.setInUse(true);
                    machine.setUserEmail(userEmail);
                    machine.setStartTime(LocalDateTime.now());
                }
                break;
            case "done":
                if (machine.getInUse()) {
                    machine.setInUse(false);
                    machine.setDone(true);
                }
                break;
            case "unload":
                if (machine.getDone()) {
                    machine.setDone(false);
                    machine.setAvailable(true);
                    machine.setUserEmail(null);
                    machine.setStartTime(null);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid action: " + action);
        }

        return laundryStatusRepository.save(machine);
    }
}
