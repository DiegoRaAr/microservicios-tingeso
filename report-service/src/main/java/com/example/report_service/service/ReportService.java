package com.example.report_service.service;

import com.example.report_service.entities.ReportEntity;
import com.example.report_service.models.GetClient;
import com.example.report_service.models.GetLoan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.report_service.repository.ReportRepository;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    ReportRepository reportRepository;

    @Autowired
    RestTemplate restTemplate;

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

    // Get client restricted with endpoint of client service
    public List<GetClient> getAllClientsRestricted(){
        GetClient[] clients = restTemplate.getForObject("http://client-service/client/restricted-clients", GetClient[].class);
        return clients != null ? Arrays.asList(clients) : new ArrayList<>();
    }

    // Get loans by range date with endpoint of loan service
    public List<GetLoan> getLoansByRangeDate(String startDate, String endDate){
        GetLoan[] loans = restTemplate.getForObject("http://loan-service/loan/loans-by-range-date/" + startDate + "/" + endDate, GetLoan[].class);
        return loans != null ? Arrays.asList(loans) : new ArrayList<>();
    }

}
