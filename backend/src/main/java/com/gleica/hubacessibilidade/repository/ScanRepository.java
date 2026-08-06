package com.gleica.hubacessibilidade.repository;

import com.gleica.hubacessibilidade.entity.ScanEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScanRepository
        extends JpaRepository<ScanEntity, Long> {

    List<ScanEntity> findAllByOrderByAnalyzedAtDesc();

    @EntityGraph(attributePaths = "issues")
    Optional<ScanEntity> findWithIssuesById(Long id);
}