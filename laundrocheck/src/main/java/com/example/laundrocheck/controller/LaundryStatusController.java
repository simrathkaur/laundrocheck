package com.example.laundrocheck.controller;

import com.example.laundrocheck.model.LaundryStatus;
import com.example.laundrocheck.repository.LaundryStatusRepository;
import com.example.laundrocheck.service.EmailService;
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
    private final EmailService emailService;

    @Autowired
    public LaundryStatusController(LaundryStatusRepository laundryStatusRepository, EmailService emailService) {
        this.laundryStatusRepository = laundryStatusRepository;
        this.emailService = emailService;
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
                    if (machine.getUserEmail() != null) {
                        emailService.sendSimpleMessage(
                                machine.getUserEmail(),
                                "Laundry Done",
                                "Your laundry in machine " + machine.getId() + " is done."
                        );
                    }
                }
                break;
            case "unload":
                if (machine.getDone()) {
                    if (machine.getUserEmail() != null) {
                        emailService.sendSimpleMessage(
                                machine.getUserEmail(),
                                "Laundry Unloaded",
                                "Your clothes are unloaded from machine " + machine.getId() + ". If it was not you, please pick up."
                        );
                    }
                    machine.setDone(false);
                    machine.setAvailable(true);
                    machine.setUserEmail(null);
                    machine.setStartTime(null);
                    emailService.notifyInterestedUsers(machine.getId().toString());
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid action: " + action);
        }

        return laundryStatusRepository.save(machine);
    }
}
