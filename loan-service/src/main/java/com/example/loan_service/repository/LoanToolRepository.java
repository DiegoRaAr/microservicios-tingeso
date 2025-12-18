package com.example.loan_service.repository;

import com.example.loan_service.entities.LoanToolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanToolRepository extends JpaRepository<LoanToolEntity, Long> {
    @Query("SELECT lt.idTool FROM LoanToolEntity lt WHERE lt.idLoan = :idLoan")
    List<Long> findAllByIdLoan(@Param("idLoan") Long idLoan);
}
