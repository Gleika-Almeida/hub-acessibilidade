package com.gleica.hubacessibilidade.controller;

import com.gleica.hubacessibilidade.dto.ScanHistoryDetailResponse;
import com.gleica.hubacessibilidade.dto.ScanHistoryItemResponse;
import com.gleica.hubacessibilidade.service.ScanHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/scans")
public class ScanHistoryController {

    private final ScanHistoryService historyService;

    public ScanHistoryController(
            ScanHistoryService historyService
    ) {
        this.historyService = historyService;
    }

    @GetMapping
    public List<ScanHistoryItemResponse> findAll() {
        return historyService.findAll();
    }

    @GetMapping("/{id}")
    public ScanHistoryDetailResponse findById(
            @PathVariable Long id
    ) {
        return historyService.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable Long id
    ) {
        historyService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
