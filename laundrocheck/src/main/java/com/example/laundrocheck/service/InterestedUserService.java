package com.example.laundrocheck.service;

import com.example.laundrocheck.model.InterestedUser;
import com.example.laundrocheck.repository.InterestedUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InterestedUserService {

    private final InterestedUserRepository interestedUserRepository;

    @Autowired
    public InterestedUserService(InterestedUserRepository interestedUserRepository) {
        this.interestedUserRepository = interestedUserRepository;
    }

    public InterestedUser addInterestedUser(String email) {
        InterestedUser user = new InterestedUser();
        user.setEmail(email);
        return interestedUserRepository.save(user);
    }

    public void removeInterestedUserByEmail(String email) {
        interestedUserRepository.findByEmail(email).ifPresent(interestedUserRepository::delete);
    }
}
