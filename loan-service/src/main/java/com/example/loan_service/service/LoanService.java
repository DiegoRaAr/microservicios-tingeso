package com.example.loan_service.service;

import com.example.loan_service.entities.LoanEntity;
import com.example.loan_service.models.GetClient;
import com.example.loan_service.repository.LoanRepository;
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

    public List<LoanEntity> findByRutClient(String rut) {

        String url = String.format("http://client-service/client/by-rut/%s", rut);
        GetClient client = restTemplate.getForObject(url, GetClient.class);
        Long idClient = client.getIdClient();

        return loanRepository.findByIdClient(idClient);
    }
}
