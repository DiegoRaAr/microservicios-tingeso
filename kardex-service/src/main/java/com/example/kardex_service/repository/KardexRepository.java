package com.example.kardex_service.repository;

import com.example.kardex_service.entities.KardexEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

// Class KardexRepository, this class extends JpaRepository and is used to interact with the kardex table in the database.
@Repository
public interface KardexRepository extends JpaRepository<KardexEntity,Long> {
    // Function to find kardex by id.
    // Input: id of kardex(Long)
    // Output: kardex entity
    public KardexEntity findByIdKardex(Long id);

    // Function to find kardex by date.
    // Input: start date and end date(Date)
    // Output: list of kardex entities
    List<KardexEntity> findByDateKardexBetween(Date startdate, Date enddate);
}
