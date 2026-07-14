package com.gleica.hubacessibilidade.dto;

import java.time.Instant;

public record ScanHtmlResponse(
    int score,
    boolean hasLanguage,
    boolean hasTitle,
    boolean hasHeadingOne,
    int totalImages,
    int imgesWithoutAlt,
    Instant analyzedAt
){    
}
