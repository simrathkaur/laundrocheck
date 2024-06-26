package com.example.laundrocheck.service;
import com.example.laundrocheck.model.InterestedUser;
import com.example.laundrocheck.repository.InterestedUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final InterestedUserRepository interestedUserRepository;


    @Autowired
    private JavaMailSender emailSender;

    public EmailService(InterestedUserRepository interestedUserRepository) {
        this.interestedUserRepository = interestedUserRepository;
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        // Extract email if necessary
        if (to.startsWith("{")) {
            to = extractEmail(to);
        }

        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
    public void notifyInterestedUsers(String machineId) {
        List<InterestedUser> interestedUsers = interestedUserRepository.findAll();
        for (InterestedUser user : interestedUsers) {
            sendSimpleMessage(
                    user.getEmail(),
                    "Machine Available",
                    "A machine is now available. Machine ID: " + machineId
            );
        }
    }

    private String extractEmail(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> emailMap = objectMapper.readValue(jsonString, Map.class);
            return emailMap.get("email");
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract email from JSON", e);
        }
    }
}
