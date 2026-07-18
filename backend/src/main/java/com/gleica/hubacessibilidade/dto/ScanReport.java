package com.gleica.hubacessibilidade.dto;

import java.time.Instant;
import java.util.List;

public record ScanReport(
    int score,
     ScanSummary summary,
     List<AccessibilityIssue> issues,
     Instant analyzedAt
) {    
}
