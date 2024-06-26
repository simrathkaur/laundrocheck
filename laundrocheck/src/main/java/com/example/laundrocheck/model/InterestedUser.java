package com.example.laundrocheck.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
@Entity
public class InterestedUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;

    public void setEmail(String email) {
        this.email=email;
    }


    public String getEmail() {
        return email;
    }
}
