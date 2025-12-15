package com.example.kardex_service.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

// Entity Kardex, this class represents the kardex table in the database.
// Kardex is a table that stores the changes of the tools.
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KardexEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idKardex;

    private Date dateKardex;
    private String stateTool;
    private Long idTool;
    private String nameTool;
}