package com.example.report_service.controller;

import com.example.report_service.entities.ReportEntity;
import com.example.report_service.models.GetClient;
import com.example.report_service.models.GetLoan;
import com.example.report_service.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    // Get all restricted clients (MUST be before /{id})
    @GetMapping("/restricted-clients")
    public ResponseEntity<List<GetClient>> getRestrictedClients() {
        List<GetClient> restrictedClients = reportService.getAllClientsRestricted();
        return ResponseEntity.ok(restrictedClients);
    }

    // Get loans by range date (MUST be before /{id})
    @GetMapping("/loans-by-range-date/{startDate}/{endDate}")
    public ResponseEntity<List<GetLoan>> getLoansByRangeDate(
            @PathVariable String startDate,
            @PathVariable String endDate) {
        List<GetLoan> loans = reportService.getLoansByRangeDate(startDate, endDate);
        return ResponseEntity.ok(loans);
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
