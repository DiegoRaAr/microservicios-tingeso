package com.example.price_service.repository;

import com.example.price_service.entities.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface PriceRepository extends JpaRepository<PriceEntity,Long> {
    ArrayList<PriceEntity> findByIdPrice(Long idPrice);
}
