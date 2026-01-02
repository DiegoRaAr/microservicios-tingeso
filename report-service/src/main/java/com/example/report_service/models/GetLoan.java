package com.example.report_service.models;

import java.time.LocalTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetLoan {
    private Long idLoan;
    private Date initDate;
    private LocalTime hourLoan;
    private Date endDate;
    private String stateLoan;
    private int penaltyLoan;
    private Long idPrice;
    private Long idClient;
}
