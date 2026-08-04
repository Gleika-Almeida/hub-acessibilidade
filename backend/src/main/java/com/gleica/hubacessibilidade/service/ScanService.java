package com.gleica.hubacessibilidade.service;

import com.gleica.hubacessibilidade.dto.ScanReport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScanService {

    private final HtmlAccessibilityAnalyzer analyzer;
    private final ScanPersistenceService persistenceService;

    public ScanService(
            HtmlAccessibilityAnalyzer analyzer,
            ScanPersistenceService persistenceService
    ) {
        this.analyzer = analyzer;
        this.persistenceService = persistenceService;
    }

    @Transactional
    public ScanReport analyzeHtml(String html) {
        ScanReport report = analyzer.analyze(html);

        persistenceService.saveHtmlScan(
                html,
                report
        );

        return report;
    }
}