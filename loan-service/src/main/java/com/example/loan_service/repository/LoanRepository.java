package com.example.loan_service.repository;

import com.example.loan_service.entities.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

// Class LoanRepository, this class extends JpaRepository and is used to interact with the loan table in the database.
@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, Long> {
    // Function to find loan by id.
    // Input: id of loan(Long)
    // Output: loan entity
    public Optional<LoanEntity> findByIdLoan(Long loanId);

    // Function to find loan by init date.
    // Input: start date and end date(Date)
    // Output: list of loan entities
    List<LoanEntity> findByInitDateBetween(Date start, Date end);

    // Function to find loan by state.
    // Input: state of loan(String)
    // Output: list of loan entities
    List<LoanEntity> findByStateLoan(String state);

    List<LoanEntity> findByIdClient(Long idClient);

}
