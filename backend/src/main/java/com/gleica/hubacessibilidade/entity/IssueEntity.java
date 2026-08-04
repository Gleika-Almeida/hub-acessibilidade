package com.gleica.hubacessibilidade.entity;

import com.gleica.hubacessibilidade.model.Severity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "issues")
public class IssueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "scan_id",
            nullable = false
    )
    private ScanEntity scan;

    @Column(
            name = "rule_id",
            nullable = false,
            length = 100
    )
    private String ruleId;

    @Column(
            nullable = false,
            length = 200
    )
    private String title;

    @Column(
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20
    )
    private Severity severity;

    @Column(
            name = "wcag_criterion",
            nullable = false,
            length = 50
    )
    private String wcagCriterion;

    @Column(columnDefinition = "TEXT")
    private String selector;

    @Column(columnDefinition = "TEXT")
    private String snippet;

    @Column(
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String recommendation;

    @Column(
            name = "created_at",
            nullable = false,
            insertable = false,
            updatable = false
    )
    private Instant createdAt;

    protected IssueEntity() {
    }

    public IssueEntity(
            String ruleId,
            String title,
            String description,
            Severity severity,
            String wcagCriterion,
            String selector,
            String snippet,
            String recommendation
    ) {
        this.ruleId = ruleId;
        this.title = title;
        this.description = description;
        this.severity = severity;
        this.wcagCriterion = wcagCriterion;
        this.selector = selector;
        this.snippet = snippet;
        this.recommendation = recommendation;
    }

    void setScan(ScanEntity scan) {
        this.scan = scan;
    }

    public Long getId() {
        return id;
    }

    public ScanEntity getScan() {
        return scan;
    }

    public String getRuleId() {
        return ruleId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getWcagCriterion() {
        return wcagCriterion;
    }

    public String getSelector() {
        return selector;
    }

    public String getSnippet() {
        return snippet;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}