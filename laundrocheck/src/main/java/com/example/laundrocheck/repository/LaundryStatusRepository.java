package com.example.laundrocheck.repository;

import com.example.laundrocheck.model.LaundryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LaundryStatusRepository extends JpaRepository<LaundryStatus, Long> {
}
