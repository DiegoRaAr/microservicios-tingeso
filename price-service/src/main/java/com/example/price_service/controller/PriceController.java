package com.example.price_service.controller;

import com.example.price_service.entities.PriceEntity;
import com.example.price_service.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/price")
public class PriceController {
    @Autowired
    PriceService priceService;

    // Get all prices
    @GetMapping("/")
    public ResponseEntity<ArrayList<PriceEntity>> listPrice(){
        ArrayList<PriceEntity> prices = priceService.getAllPrice();
        return ResponseEntity.ok(prices);
    }

    // Get price by id
    @GetMapping("/{id}")
    public ResponseEntity<PriceEntity> getPriceById(@PathVariable Long id){
        PriceEntity price = priceService.findByIdPrice(id);
        return ResponseEntity.ok(price);
    }

    // Update price
    @PutMapping("/")
    public ResponseEntity<PriceEntity> updatePrice(@RequestBody PriceEntity price){
        PriceEntity priceUpdated = priceService.updatePrice(price);
        return ResponseEntity.ok(priceUpdated);
    }

    // Delete price by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deletePriceByID(@PathVariable Long id) throws Exception{
        var isDeleted = priceService.deletePrice(id);
        return ResponseEntity.noContent().build();
    }
}
