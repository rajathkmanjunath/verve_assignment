package com.example.demo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "unique_requests")
@Data
public class UniqueRequest {
    @Id
    @Column(unique = true)
    private Integer requestId;
}