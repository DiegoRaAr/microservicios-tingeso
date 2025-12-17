package com.example.kardex_service.controller;

import com.example.kardex_service.entities.KardexEntity;
import com.example.kardex_service.services.KardexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kardex")
public class KardexController {
    @Autowired
    KardexService kardexService;

    // Get all kardexes
    @GetMapping("/")
    public ResponseEntity<List<KardexEntity>> listKardexes(){
        List<KardexEntity> kardex = kardexService.getKardexes();
        return ResponseEntity.ok(kardex);
    }

    // Get kardex by id
    @GetMapping("/{id}")
    public ResponseEntity<KardexEntity> getKardexByID(@PathVariable Long id){
        KardexEntity kardex = kardexService.getKardexById(id);
        return ResponseEntity.ok(kardex);
    }

    // Create kardex
    @PostMapping("/")
    public ResponseEntity<KardexEntity> saveKardex(@RequestBody KardexEntity kardexEntity){
        System.out.println("📩 Kardex recibido: " + kardexEntity);  // ← Agrega esto
        KardexEntity newKardex = kardexService.createKardex(kardexEntity);
        return ResponseEntity.ok(newKardex);
    }

    // Update kardex
    @PutMapping("/")
    public ResponseEntity<KardexEntity> updateKardex(@RequestBody KardexEntity kardexEntity){
        KardexEntity kardexUpdate = kardexService.updateKardex(kardexEntity);
        return ResponseEntity.ok(kardexUpdate);
    }

    // Delete kardex by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteKardexByID(@PathVariable Long id)throws Exception{
        var isDeleted = kardexService.deleteKardex(id);
        return ResponseEntity.noContent().build();
    }
}