package com.example.report_service.controller;

import com.example.report_service.entities.ReportEntity;
import com.example.report_service.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    ReportService reportService;

    // Get all reports
    @GetMapping("/")
    public ResponseEntity<ArrayList<ReportEntity>> listReports() {
        ArrayList<ReportEntity> reports = reportService.getAllReports();
        return ResponseEntity.ok(reports);
    }

    // Get report by id
    @GetMapping("/{id}")
    public ResponseEntity<ReportEntity> getReportById(@PathVariable Long id) {
        ReportEntity report = reportService.findByIdReport(id);
        return ResponseEntity.ok(report);
    }

    // Create report
    @PostMapping("/")
    public ResponseEntity<ReportEntity> createReport(@RequestBody ReportEntity report) {
        ReportEntity reportCreated = reportService.saveReport(report);
        return ResponseEntity.ok(reportCreated);
    }

    // Update report
    @PutMapping("/")
    public ResponseEntity<ReportEntity> updateReport(@RequestBody ReportEntity report) {
        ReportEntity reportUpdated = reportService.updateReport(report);
        return ResponseEntity.ok(reportUpdated);
    }

    // Delete report by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteReportByID(@PathVariable Long id) throws Exception {
        var isDeleted = reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}
