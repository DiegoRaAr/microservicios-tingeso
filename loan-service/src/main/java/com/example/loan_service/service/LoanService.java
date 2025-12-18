package com.example.loan_service.service;

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
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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
