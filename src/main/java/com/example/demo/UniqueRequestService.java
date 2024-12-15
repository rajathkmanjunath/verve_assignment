package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class UniqueRequestService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private UniqueRequestRepository repository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public ResponseEntity<String> processRequest(int id, String endpoint) {
        log.info("Processing request for id: {}", id);
        try {
            UniqueRequest request = new UniqueRequest();
            request.setRequestId(id);
            repository.save(request); // This will throw exception if id already exists

            if (endpoint != null) {
                long count = repository.count();
                restTemplate.getForEntity(endpoint + "?count=" + count, String.class);
            }
            return ResponseEntity.ok("ok");

        } catch (DataIntegrityViolationException e) {
            // This exception occurs when trying to insert a duplicate id
            log.info("Duplicate request received for id: {}", id);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("duplicate request");

        } catch (Exception e) {
            log.error("Error processing request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("failed");
        }
    }

    public void logAndSendUniqueRequestCount() {
        long uniqueCount = repository.count();
        log.info("Unique request count: {}", uniqueCount);
        repository.deleteAllRequests();
        kafkaTemplate.send("unique-request-count", String.valueOf(uniqueCount));
    }
}