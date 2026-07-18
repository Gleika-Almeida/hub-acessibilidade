package com.gleica.hubacessibilidade.dto;

public record ScanSummary(
    int total,
    int critical,
    int serious,
    int moderate,
    int minor
){ 
}
