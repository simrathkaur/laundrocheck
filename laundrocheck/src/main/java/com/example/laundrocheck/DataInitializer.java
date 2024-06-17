package com.example.laundrocheck;

import com.example.laundrocheck.model.LaundryStatus;
import com.example.laundrocheck.repository.LaundryStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    private final LaundryStatusRepository laundryStatusRepository;

    @Autowired
    public DataInitializer(LaundryStatusRepository laundryStatusRepository) {
        this.laundryStatusRepository = laundryStatusRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        initializeLaundryStatus();
    }

    private void initializeLaundryStatus() {
        // Create two LaundryStatus instances
        LaundryStatus status1 = new LaundryStatus();
        status1.setAvailable(true);
        status1.setInUse(false);
        status1.setDone(false);
        status1.setUserEmail(null);
        status1.setStartTime(LocalDateTime.now().minusHours(1)); // Set a random start time

        LaundryStatus status2 = new LaundryStatus();
        status2.setAvailable(true);
        status2.setInUse(false);
        status2.setDone(false);
        status2.setUserEmail(null);
        status2.setStartTime(LocalDateTime.now().minusHours(2)); // Set a random start time

        // Save both instances to the database
        laundryStatusRepository.saveAll(Arrays.asList(status1, status2));
    }
}
