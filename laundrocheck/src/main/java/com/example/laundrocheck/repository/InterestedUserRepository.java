package com.example.laundrocheck.repository;

import com.example.laundrocheck.model.InterestedUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InterestedUserRepository extends JpaRepository<InterestedUser, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM InterestedUser iu WHERE iu.email = :email")
    void deleteByEmail(@Param("email") String email);
    Optional<InterestedUser> findByEmail(String email);

}
