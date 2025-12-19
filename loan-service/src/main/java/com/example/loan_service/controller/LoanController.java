package com.example.loan_service.controller;

import com.example.loan_service.entities.LoanEntity;
import com.example.loan_service.entities.LoanToolEntity;
import com.example.loan_service.models.LoanRequest;
import com.example.loan_service.models.Tool;
import com.example.loan_service.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    public ResponseEntity<LoanEntity> createLoan(@RequestBody LoanRequest loanRequest) throws Exception {
        LoanEntity newLoan = loanService.saveLoan(loanRequest.getLoan(), loanRequest.getToolIds());
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

    // Get tools by loan id
    @GetMapping("/tools/{idLoan}")
    public ResponseEntity<List<Tool>> getToolsByLoanId(@PathVariable Long idLoan) {
        List<Tool> tools = loanService.findToolById(idLoan);
        return ResponseEntity.ok(tools);
    }

    // Get loans by range date
    @GetMapping("/loans-by-range-date/{initDate}/{endDate}")
    public ResponseEntity<List<LoanEntity>> getLoansByRangeDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date initDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) throws Exception {
        return ResponseEntity.ok(loanService.getLoansByDateRange(initDate, endDate));
    }

    // Update penalty loan
    @PutMapping("/update-penalty/{id}")
    public ResponseEntity<LoanEntity> updatePenaltyLoan(@PathVariable Long id) {
        LoanEntity loanUpdated = loanService.updatePenaltyLoan(id);
        return ResponseEntity.ok(loanUpdated);
    }

    // Finish loan
    @PutMapping("/finish-loan/{id}/{totalValue}")
    public ResponseEntity<LoanEntity> finishLoan(@PathVariable Long id, @PathVariable Integer totalValue)
            throws Exception {
        return ResponseEntity.ok(loanService.finalizeLoan(id, totalValue));
    }

    ////////////////// LOAN TOOL //////////////////

    // Create loan-tool association
    @PostMapping("/loan-tool")
    public ResponseEntity<LoanToolEntity> createLoanTool(@RequestBody LoanToolEntity loanTool) {
        LoanToolEntity newLoanTool = loanService.saveLoanTool(loanTool);
        return ResponseEntity.ok(newLoanTool);
    }

    // Get all loan-tool associations
    @GetMapping("/loan-tool")
    public ResponseEntity<List<LoanToolEntity>> listLoanTools() {
        List<LoanToolEntity> loanTools = loanService.getAllLoanTools();
        return ResponseEntity.ok(loanTools);
    }

    // Delete loan-tool association
    @DeleteMapping("/loan-tool/{id}")
    public ResponseEntity<Boolean> deleteLoanTool(@PathVariable Long id) throws Exception {
        loanService.deleteLoanTool(id);
        return ResponseEntity.noContent().build();
    }
}
