package com.example.client_service.repository;

import com.example.client_service.entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// Class ClientRepository, this class extends JpaRepository and is used to interact with the client table in the database.
@Repository
public interface ClientRepository extends JpaRepository<ClientEntity,Long> {
    // Function to find client by rut.
    // Input: rut of client(String)
    // Output: client entity
    @Query("SELECT c FROM ClientEntity c WHERE c.rutClient = :rut")
    public ClientEntity findByRutClient(@Param("rut") String rut);

    // Function to find client by id.
    // Input: id of client(Long)
    // Output: client entity
    public ClientEntity findByIdClient(Long id);

    // Function to find client by name.
    // Input: name of client(String)
    // Output: client entity
    public ClientEntity findByNameClient(String name);

    // Function to find client by state.
    // Input: state of client(String)
    // Output: list of client entities
    public ClientEntity findAByStateClient(String state);
}
