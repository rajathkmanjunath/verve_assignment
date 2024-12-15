package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UniqueRequestRepository extends JpaRepository<UniqueRequest, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM UniqueRequest u")
    void deleteAllRequests();
}