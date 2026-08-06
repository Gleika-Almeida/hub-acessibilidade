package com.gleica.hubacessibilidade.service;

import com.gleica.hubacessibilidade.dto.AccessibilityIssue;
import com.gleica.hubacessibilidade.dto.ScanHistoryDetailResponse;
import com.gleica.hubacessibilidade.dto.ScanHistoryItemResponse;
import com.gleica.hubacessibilidade.dto.ScanSummary;
import com.gleica.hubacessibilidade.entity.IssueEntity;
import com.gleica.hubacessibilidade.entity.ScanEntity;
import com.gleica.hubacessibilidade.exception.ScanNotFoundException;
import com.gleica.hubacessibilidade.repository.ScanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ScanHistoryService {

    private final ScanRepository scanRepository;

    public ScanHistoryService(
            ScanRepository scanRepository
    ) {
        this.scanRepository = scanRepository;
    }

    @Transactional(readOnly = true)
    public List<ScanHistoryItemResponse> findAll() {
        return scanRepository
                .findAllByOrderByAnalyzedAtDesc()
                .stream()
                .map(this::toHistoryItem)
                .toList();
    }

    @Transactional(readOnly = true)
    public ScanHistoryDetailResponse findById(
            Long id
    ) {
        ScanEntity scan = scanRepository
                .findWithIssuesById(id)
                .orElseThrow(
                        () -> new ScanNotFoundException(id)
                );

        return toHistoryDetail(scan);
    }

    @Transactional
    public void deleteById(Long id) {
        ScanEntity scan = scanRepository
                .findById(id)
                .orElseThrow(
                        () -> new ScanNotFoundException(id)
                );

        scanRepository.delete(scan);
    }

    private ScanHistoryItemResponse toHistoryItem(
            ScanEntity scan
    ) {
        return new ScanHistoryItemResponse(
                scan.getId(),
                scan.getSourceType(),
                scan.getScore(),
                createSummary(scan),
                scan.getAnalyzedAt(),
                scan.getCreatedAt()
        );
    }

    private ScanHistoryDetailResponse toHistoryDetail(
            ScanEntity scan
    ) {
        List<AccessibilityIssue> issues = scan
                .getIssues()
                .stream()
                .map(this::toAccessibilityIssue)
                .toList();

        return new ScanHistoryDetailResponse(
                scan.getId(),
                scan.getSourceType(),
                scan.getSourceValue(),
                scan.getScore(),
                createSummary(scan),
                issues,
                scan.getAnalyzedAt(),
                scan.getCreatedAt()
        );
    }

    private ScanSummary createSummary(
            ScanEntity scan
    ) {
        return new ScanSummary(
                scan.getTotalIssues(),
                scan.getCriticalIssues(),
                scan.getSeriousIssues(),
                scan.getModerateIssues(),
                scan.getMinorIssues()
        );
    }

    private AccessibilityIssue toAccessibilityIssue(
            IssueEntity issue
    ) {
        return new AccessibilityIssue(
                issue.getRuleId(),
                issue.getTitle(),
                issue.getDescription(),
                issue.getSeverity(),
                issue.getWcagCriterion(),
                issue.getSelector(),
                issue.getSnippet(),
                issue.getRecommendation()
        );
    }
}