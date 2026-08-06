package com.gleica.hubacessibilidade.dto;

import com.gleica.hubacessibilidade.model.SourceType;

import java.time.Instant;
import java.util.List;

public record ScanHistoryDetailResponse(
        Long id,
        SourceType sourceType,
        String sourceValue,
        int score,
        ScanSummary summary,
        List<AccessibilityIssue> issues,
        Instant analyzedAt,
        Instant createdAt
) {
}
