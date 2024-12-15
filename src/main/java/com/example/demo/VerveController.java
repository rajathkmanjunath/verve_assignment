package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/verve")
public class VerveController {

    @Autowired
    private UniqueRequestService uniqueRequestService;

    @GetMapping("/accept")
    public ResponseEntity<String> acceptRequest(@RequestParam int id, @RequestParam(required = false) String endpoint) {
        log.info("Received request with id: {}", id);
        return uniqueRequestService.processRequest(id, endpoint);
    }

    @Scheduled(fixedRate = 60000)
    public void logUniqueRequestCount() {
        log.info("Logging unique request count");
        uniqueRequestService.logAndSendUniqueRequestCount();
    }
}