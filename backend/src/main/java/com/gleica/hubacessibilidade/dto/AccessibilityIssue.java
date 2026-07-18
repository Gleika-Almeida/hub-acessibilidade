package com.gleica.hubacessibilidade.dto;

import com.gleica.hubacessibilidade.model.Severity;

public record AccessibilityIssue(
    String ruleId,
    String title,
    String description,
    Severity severity,
    String wcagCriterion,
    String selector,
    String snippet,
    String recommendation
){   
}
