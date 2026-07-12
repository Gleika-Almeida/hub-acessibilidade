package com.gleica.hubacessibilidade.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping
    public Map<String, Object> checkHealth() {
        return Map.of(
                "status", "UP",
                "application", "Hub Acessibilidade API",
                "timestamp", Instant.now()
        );
    }
}
