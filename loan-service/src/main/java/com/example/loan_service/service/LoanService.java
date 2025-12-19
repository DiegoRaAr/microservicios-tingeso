package com.example.loan_service.service;

import ch.qos.logback.core.net.server.Client;
import com.example.loan_service.entities.LoanEntity;
import com.example.loan_service.entities.LoanToolEntity;
import com.example.loan_service.models.GetClient;
import com.example.loan_service.models.Tool;
import com.example.loan_service.repository.LoanRepository;
import com.example.loan_service.repository.LoanToolRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LoanToolRepository loanToolRepository;

    @Autowired
    RestTemplate restTemplate;

    // Find Loan
    public ArrayList<LoanEntity> getLoans() {
        return (ArrayList<LoanEntity>) loanRepository.findAll();
    }

    // Create Loan
    public LoanEntity saveLoan(LoanEntity loanEntity) {
        return loanRepository.save(loanEntity);
    }

    // Find Loan by Id
    public Optional<LoanEntity> findById(Long id) {
        return loanRepository.findById(id);
    }

    // Update Loan
    public LoanEntity updateLoan(LoanEntity loanEntity) {
        return loanRepository.save(loanEntity);
    }

    // Delete Loam by id
    public boolean deleteLoan(Long id) throws Exception {
        try {
            loanRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    // Loans by rut of client
    public List<LoanEntity> findByRutClient(String rut) {

        String url = String.format("http://client-service/client/by-rut/%s", rut);
        GetClient client = restTemplate.getForObject(url, GetClient.class);
        Long idClient = client.getIdClient();

        return loanRepository.findByIdClient(idClient);
    }

    // Get tool by id loan
    public List<Tool> findToolById(Long id) {
        List<Long> idsTool = loanToolRepository.findAllByIdLoan(id);

        List<Tool> tools = idsTool.stream()
                .map(idTool -> {
                    String url = String.format("http://tool-service/tool/%d", idTool);
                    return restTemplate.getForObject(url, Tool.class);
                })
                .toList();

        return tools;
    }

    // Get all loans by date range
    public List<LoanEntity> getLoansByDateRange(Date startDate, Date endDate) throws Exception {
        if (startDate == null || endDate == null) {
            throw new Exception("Las fechas no pueden ser nulas");
        }
        if (endDate.before(startDate)) {
            throw new Exception("La fecha de fin no puede ser anterior a la fecha de inicio");
        }

        List<LoanEntity> allLoans = loanRepository.findAll();
        List<LoanEntity> filteredLoans = new ArrayList<>();

        for (LoanEntity loan : allLoans) {
            Date initDate = loan.getInitDate();
            if (initDate != null && !initDate.before(startDate) && !initDate.after(endDate)) {
                filteredLoans.add(loan);
            }
        }
        return filteredLoans;
    }

    @Transactional
    public LoanEntity updatePenaltyLoan(Long id) {
        // Search loan by id
        LoanEntity loanEntity = loanRepository.findByIdLoan(id)
                .orElseThrow(() -> new RuntimeException("Loan not found with id: " + id));
        Long idClient = loanEntity.getIdClient();

        // Get client of loan
        String url = String.format("http://client-service/client/%d", idClient);
        GetClient client = restTemplate.getForObject(url, GetClient.class);

        // Search finish date of loan
        Date finishDate = loanEntity.getEndDate();
        // Search today date
        Date todayDate = new Date();

        // Verify if loan is finished
        if (loanEntity.getStateLoan().equals("FINALIZADO")) {
            return loanEntity;
        }

        // Verify if loan is finished and update penalty
        if (finishDate.before(todayDate)) {
            long diffMillis = todayDate.getTime() - finishDate.getTime();
            long diffDays = diffMillis / (1000 * 60 * 60 * 24);

            // Get all tools of loan
            List<Long> idsTool = loanToolRepository.findAllByIdLoan(id);
            List<Tool> tools = idsTool.stream()
                    .map(idTool -> {
                        String url2 = String.format("http://tool-service/tool/%d", idTool);
                        return restTemplate.getForObject(url2, Tool.class);
                    })
                    .toList();

            // Calculate total penalty
            int totalPenalty = 0;
            for (Tool tool : tools) {
                totalPenalty += tool.getLateCharge() * diffDays;
            }

            // Update state client
            client.setStateClient("RESTRINGIDO");
            String updateUrl = String.format("http://client-service/client/%d", client.getIdClient());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            org.springframework.http.HttpEntity<GetClient> requestEntity = new org.springframework.http.HttpEntity<>(
                    client, headers);
            restTemplate.put(updateUrl, requestEntity);

            // Update loan
            loanEntity.setPenaltyLoan(totalPenalty);
            loanRepository.save(loanEntity);
            return loanEntity;
        }

        return loanEntity;
    }

    ////////////////////// LOAN TOOL //////////////////////

    // Create LoanTool association
    public LoanToolEntity saveLoanTool(LoanToolEntity loanToolEntity) {
        return loanToolRepository.save(loanToolEntity);
    }

    // Get all LoanTools
    public ArrayList<LoanToolEntity> getAllLoanTools() {
        return (ArrayList<LoanToolEntity>) loanToolRepository.findAll();
    }

    // Delete LoanTool by id
    public boolean deleteLoanTool(Long id) throws Exception {
        try {
            loanToolRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
