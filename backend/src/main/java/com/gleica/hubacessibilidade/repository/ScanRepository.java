package com.gleica.hubacessibilidade.repository;

import com.gleica.hubacessibilidade.entity.ScanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScanRepository
        extends JpaRepository<ScanEntity, Long> {

    List<ScanEntity> findAllByOrderByAnalyzedAtDesc();
}