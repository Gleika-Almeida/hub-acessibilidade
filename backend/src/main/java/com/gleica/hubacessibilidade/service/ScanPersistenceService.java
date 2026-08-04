package com.gleica.hubacessibilidade.service;

import com.gleica.hubacessibilidade.dto.AccessibilityIssue;
import com.gleica.hubacessibilidade.dto.ScanReport;
import com.gleica.hubacessibilidade.entity.IssueEntity;
import com.gleica.hubacessibilidade.entity.ScanEntity;
import com.gleica.hubacessibilidade.model.SourceType;
import com.gleica.hubacessibilidade.repository.ScanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScanPersistenceService {

    private final ScanRepository scanRepository;

    public ScanPersistenceService(
            ScanRepository scanRepository
    ) {
        this.scanRepository = scanRepository;
    }

    @Transactional
    public Long saveHtmlScan(
            String html,
            ScanReport report
    ) {
        ScanEntity scan = new ScanEntity(
                SourceType.HTML,
                html,
                report.score(),
                report.summary().total(),
                report.summary().critical(),
                report.summary().serious(),
                report.summary().moderate(),
                report.summary().minor(),
                report.analyzedAt()
        );

        for (
                AccessibilityIssue issue
                : report.issues()
        ) {
            IssueEntity issueEntity =
                    createIssueEntity(issue);

            scan.addIssue(issueEntity);
        }

        ScanEntity savedScan =
                scanRepository.save(scan);

        return savedScan.getId();
    }

    private IssueEntity createIssueEntity(
            AccessibilityIssue issue
    ) {
        return new IssueEntity(
                issue.ruleId(),
                issue.title(),
                issue.description(),
                issue.severity(),
                issue.wcagCriterion(),
                issue.selector(),
                issue.snippet(),
                issue.recommendation()
        );
    }
}