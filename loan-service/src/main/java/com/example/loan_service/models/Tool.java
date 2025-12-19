package com.example.loan_service.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tool {
    private Long idTool;
    private String nameTool;
    private String categoryTool;
    private int totalValue;
    private String stateTool;
    private int stockTool;
    private int repairCharge;
    private int dailyCharge;
    private int lateCharge;
}