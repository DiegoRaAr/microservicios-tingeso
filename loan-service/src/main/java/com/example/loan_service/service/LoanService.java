package com.example.loan_service.service;

import com.example.loan_service.entities.LoanEntity;
import com.example.loan_service.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;

    // Find Loan
    public ArrayList<LoanEntity> getLoans(){
        return (ArrayList<LoanEntity>) loanRepository.findAll();
    }

    // Find Loan by Id
    public Optional<LoanEntity> findById(Long id){
        return loanRepository.findById(id);
    }

    //Update Loan
    public LoanEntity updateLoan(LoanEntity loanEntity){
        return loanRepository.save(loanEntity);
    }

    // Delete Loam by id
    public boolean deleteLoan(Long id) throws Exception{
        try {
            loanRepository.deleteById(id);
            return true;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
