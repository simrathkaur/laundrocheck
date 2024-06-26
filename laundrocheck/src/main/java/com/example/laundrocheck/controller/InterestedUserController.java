package com.example.laundrocheck.controller;

import com.example.laundrocheck.model.InterestedUser;
import com.example.laundrocheck.repository.InterestedUserRepository;
import com.example.laundrocheck.service.InterestedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interested")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class InterestedUserController {

    private final InterestedUserService interestedUserService;
    private final InterestedUserRepository interestedUserRepository;

    @Autowired
    public InterestedUserController(InterestedUserService interestedUserService, InterestedUserRepository interestedUserRepository) {
        this.interestedUserService = interestedUserService;
        this.interestedUserRepository = interestedUserRepository;
    }

    @PostMapping("/add")
    public InterestedUser addInterestedUser(@RequestBody String email) {
        return interestedUserService.addInterestedUser(email);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeInterestedUser(@RequestBody String email) {
        interestedUserService.removeInterestedUserByEmail(email);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public List<InterestedUser> getAllInterestedUsers() {
        return interestedUserRepository.findAll();
    }

    @GetMapping("/is-interested")
    public boolean isUserInterested(@RequestParam String email) {
        // Remove leading and trailing whitespace and add surrounding quotes if missing
        String cleanedEmail = email.trim();
        if (!cleanedEmail.startsWith("\"")) {
            cleanedEmail = "\"" + cleanedEmail + "\"";
        }
        return interestedUserRepository.findByEmail(cleanedEmail).isPresent();
    }



}
