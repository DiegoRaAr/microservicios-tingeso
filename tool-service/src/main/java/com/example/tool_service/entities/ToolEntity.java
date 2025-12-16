package com.example.tool_service.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entity Tool, this class represents the tool table in the database.
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToolEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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