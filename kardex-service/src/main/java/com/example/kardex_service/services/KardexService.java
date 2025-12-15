package com.example.kardex_service.services;

import com.example.kardex_service.entities.KardexEntity;
import com.example.kardex_service.repository.KardexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class KardexService {
    @Autowired
    KardexRepository kardexRepository;

    // Find Kardex's
    public ArrayList<KardexEntity> getKardexes(){
        return (ArrayList<KardexEntity>) kardexRepository.findAll();
    }

    // Create Kardex
    public KardexEntity createKardex(KardexEntity kardexEntity){
        return kardexRepository.save(kardexEntity);
    }

    // Find Kardex by Id
    public KardexEntity getKardexById(Long id){
        return kardexRepository.findById(id).get();
    }

    // Update Kardex
    public KardexEntity updateKardex(KardexEntity kardexEntity){
        return kardexRepository.save(kardexEntity);
    }

    // Delete Kardex
    public boolean deleteKardex(Long id) throws Exception{
        try {
            kardexRepository.deleteById(id);
            return true;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}