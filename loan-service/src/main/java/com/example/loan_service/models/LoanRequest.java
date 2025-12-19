package com.example.loan_service.models;

import com.example.loan_service.entities.LoanEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequest {
    private LoanEntity loan;
    private List<Long> toolIds;
}
