package com.example.report_service.repository;

import com.example.report_service.entities.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Long> {
    // Find report by id
    List<ReportEntity> findByIdReport(Long idReport);
}
