package com.example.loan_service.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Kardex {
    private Date dateKardex;
    private String stateTool;
    private Long idTool;
    private String nameTool;
}
