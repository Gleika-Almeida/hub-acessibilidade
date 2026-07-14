package com.gleica.hubacessibilidade.controller;

import com.gleica.hubacessibilidade.dto.ScanHtmlRequest;
import com.gleica.hubacessibilidade.dto.ScanHtmlResponse;
import com.gleica.hubacessibilidade.service.HtmlAccessibilityAnalyzer;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scans")
public class ScanController {

    private final HtmlAccessibilityAnalyzer analyzer;

    public ScanController(
            HtmlAccessibilityAnalyzer analyzer
    ) {
        this.analyzer = analyzer;
    }

    @PostMapping(
            value = "/html",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ScanHtmlResponse scanHtml(
            @Valid @RequestBody ScanHtmlRequest request
    ) {
        return analyzer.analyze(request.html());
    }
}
