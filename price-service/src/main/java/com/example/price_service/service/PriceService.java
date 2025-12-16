package com.example.price_service.service;

import com.example.price_service.entities.PriceEntity;
import com.example.price_service.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class PriceService {
    @Autowired
    PriceRepository priceRepository;

    // find Price by id
    public ArrayList<PriceEntity> getAllPrice(){
        return (ArrayList<PriceEntity>) priceRepository.findAll();
    }

    //Create price
    public PriceEntity savePrice(PriceEntity priceEntity){
        return priceRepository.save(priceEntity);
    }

    //Find price by id
    public PriceEntity findByIdPrice(Long idPrice){
        return priceRepository.findByIdPrice(idPrice).get(0);
    }

    //Update price
    public PriceEntity updatePrice(PriceEntity priceEntity){
        return priceRepository.save(priceEntity);
    }

    //Delete price by id
    public boolean deletePrice(Long idPrice) throws Exception{
        try {
            priceRepository.deleteById(idPrice);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
