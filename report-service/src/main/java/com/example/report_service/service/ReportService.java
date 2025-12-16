package com.example.report_service.service;

import com.example.report_service.entities.ReportEntity;
import com.example.report_service.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class ReportService {
    @Autowired
    ReportRepository reportRepository;

    // Get all reports
    public ArrayList<ReportEntity> getAllReports() {
        return (ArrayList<ReportEntity>) reportRepository.findAll();
    }

    // Create report
    public ReportEntity saveReport(ReportEntity reportEntity) {
        return reportRepository.save(reportEntity);
    }

    // Find report by id
    public ReportEntity findByIdReport(Long idReport) {
        return reportRepository.findByIdReport(idReport).get(0);
    }

    // Update report
    public ReportEntity updateReport(ReportEntity reportEntity) {
        return reportRepository.save(reportEntity);
    }

    // Delete report by id
    public boolean deleteReport(Long idReport) throws Exception {
        try {
            reportRepository.deleteById(idReport);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
