package com.example.loan_service.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Date;

// Entity Loan, this class represents the loan table in the database.
// Loan is a table that stores the loans of the tools for the clients.
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanEntity {
    //Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLoan;

    private Date initDate;
    private LocalTime hourLoan;
    private Date endDate;
    private String stateLoan;
    private int penaltyLoan;

    //Foreing key
    private Long idPrice;
    private Long idClient;
}
