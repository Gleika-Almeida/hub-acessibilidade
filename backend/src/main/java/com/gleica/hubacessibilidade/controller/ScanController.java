package com.gleica.hubacessibilidade.controller;

import com.gleica.hubacessibilidade.dto.ScanHtmlRequest;
import com.gleica.hubacessibilidade.dto.ScanReport;
import com.gleica.hubacessibilidade.service.ScanService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scans")
public class ScanController {

    private final ScanService scanService;

    public ScanController(
            ScanService scanService
    ) {
        this.scanService = scanService;
    }

    @PostMapping(
            value = "/html",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ScanReport scanHtml(
            @Valid @RequestBody ScanHtmlRequest request
    ) {
        return scanService.analyzeHtml(
                request.html()
        );
    }
}