package com.gleica.hubacessibilidade.dto;

import com.gleica.hubacessibilidade.model.SourceType;

import java.time.Instant;

public record ScanHistoryItemResponse(
        Long id,
        SourceType sourceType,
        int score,
        ScanSummary summary,
        Instant analyzedAt,
        Instant createdAt
) {
}