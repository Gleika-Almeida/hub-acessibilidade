package com.gleica.hubacessibilidade.entity;

import com.gleica.hubacessibilidade.model.SourceType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "scans")
public class ScanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "source_type",
            nullable = false,
            length = 30
    )
    private SourceType sourceType;

    @Column(
            name = "source_value",
            columnDefinition = "TEXT"
    )
    private String sourceValue;

    @Column(nullable = false)
    private int score;

    @Column(
            name = "total_issues",
            nullable = false
    )
    private int totalIssues;

    @Column(
            name = "critical_issues",
            nullable = false
    )
    private int criticalIssues;

    @Column(
            name = "serious_issues",
            nullable = false
    )
    private int seriousIssues;

    @Column(
            name = "moderate_issues",
            nullable = false
    )
    private int moderateIssues;

    @Column(
            name = "minor_issues",
            nullable = false
    )
    private int minorIssues;

    @Column(
            name = "analyzed_at",
            nullable = false
    )
    private Instant analyzedAt;

    @Column(
            name = "created_at",
            nullable = false,
            insertable = false,
            updatable = false
    )
    private Instant createdAt;

    @OneToMany(
            mappedBy = "scan",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<IssueEntity> issues =
            new ArrayList<>();

    protected ScanEntity() {
    }

    public ScanEntity(
            SourceType sourceType,
            String sourceValue,
            int score,
            int totalIssues,
            int criticalIssues,
            int seriousIssues,
            int moderateIssues,
            int minorIssues,
            Instant analyzedAt
    ) {
        this.sourceType = sourceType;
        this.sourceValue = sourceValue;
        this.score = score;
        this.totalIssues = totalIssues;
        this.criticalIssues = criticalIssues;
        this.seriousIssues = seriousIssues;
        this.moderateIssues = moderateIssues;
        this.minorIssues = minorIssues;
        this.analyzedAt = analyzedAt;
    }

    public void addIssue(IssueEntity issue) {
        issues.add(issue);
        issue.setScan(this);
    }

    public void removeIssue(IssueEntity issue) {
        issues.remove(issue);
        issue.setScan(null);
    }

    public Long getId() {
        return id;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public String getSourceValue() {
        return sourceValue;
    }

    public int getScore() {
        return score;
    }

    public int getTotalIssues() {
        return totalIssues;
    }

    public int getCriticalIssues() {
        return criticalIssues;
    }

    public int getSeriousIssues() {
        return seriousIssues;
    }

    public int getModerateIssues() {
        return moderateIssues;
    }

    public int getMinorIssues() {
        return minorIssues;
    }

    public Instant getAnalyzedAt() {
        return analyzedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public List<IssueEntity> getIssues() {
        return List.copyOf(issues);
    }
}