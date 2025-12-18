package com.example.loan_service.controller;

import com.example.loan_service.entities.LoanEntity;
import com.example.loan_service.service.LoanService;
import org.hibernate.tool.schema.spi.SourceDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/loan")
public class LoanController {
    @Autowired
    LoanService loanService;

    // Get all loans
    @GetMapping("/")
    public ResponseEntity<List<LoanEntity>> listLoan() {
        List<LoanEntity> loans = loanService.getLoans();
        return ResponseEntity.ok(loans);
    }

    // Create loan
    @PostMapping("/")
    public ResponseEntity<LoanEntity> createLoan(@RequestBody LoanEntity loan) {
        LoanEntity newLoan = loanService.saveLoan(loan);
        return ResponseEntity.ok(newLoan);
    }

    // Get loan by id
    @GetMapping("/{id}")
    public ResponseEntity<Optional<LoanEntity>> getLoanById(@PathVariable Long id) {
        Optional<LoanEntity> loan = loanService.findById(id);
        return ResponseEntity.ok(loan);
    }

    // Update loan
    @PutMapping("/")
    public ResponseEntity<LoanEntity> updateLoan(@RequestBody LoanEntity loan) {
        LoanEntity loanUpdated = loanService.updateLoan(loan);
        return ResponseEntity.ok(loanUpdated);
    }

    // Delete loan by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteLoanByID(@PathVariable Long id) throws Exception {
        var isDeleted = loanService.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }

    // Get loan by rut client
    @GetMapping("/loan-by-rut/{rut}")
    public ResponseEntity<List<LoanEntity>> getLoanByRutClient(@PathVariable String rut) {
        List<LoanEntity> loans = loanService.findByRutClient(rut);
        return ResponseEntity.ok(loans);
    }
}
