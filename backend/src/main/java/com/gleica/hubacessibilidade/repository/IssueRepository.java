package com.gleica.hubacessibilidade.repository;

import com.gleica.hubacessibilidade.entity.IssueEntity;
import com.gleica.hubacessibilidade.model.Severity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueRepository
        extends JpaRepository<IssueEntity, Long> {

    List<IssueEntity> findByScanId(Long scanId);

    long countBySeverity(Severity severity);
}