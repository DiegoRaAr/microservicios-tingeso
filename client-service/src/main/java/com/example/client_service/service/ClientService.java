package com.example.client_service.service;

import com.example.client_service.entities.ClientEntity;
import com.example.client_service.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService {
    @Autowired
    ClientRepository clientRepository;

    // Find Client
    public ArrayList<ClientEntity> getClients(){
        return (ArrayList<ClientEntity>) clientRepository.findAll();
    }

    //Create Client
    public ClientEntity saveClient(ClientEntity clientEntity){
        return clientRepository.save(clientEntity);
    }

    // find Client by Id
    public ClientEntity getClientById(Long id){
        return clientRepository.findByIdClient(id);
    }

    //Find Client by Rut
    public ClientEntity getClientByRut(String rut){
        return clientRepository.findByRutClient(rut);
    }

    // Update Client
    public ClientEntity updateClient(ClientEntity clientEntity){
        return clientRepository.save(clientEntity);
    }

    // Delete Client
    public boolean deleteClient(Long id) throws  Exception{
        try {
            clientRepository.deleteById(id);
            return true;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    // Change State Client
    public ClientEntity changeStateClient(Long id) throws Exception{
        ClientEntity client = getClientById(id);
        if (client.getStateClient().equals("ACTIVO")){
            client.setStateClient("RESTRINGIDO");
        } else {
            client.setStateClient("ACTIVO");
        }
        return clientRepository.save(client);
    }

    // Get client with state "RESTRINGIDO"
    public ArrayList<ClientEntity> getRestrictedClients(){
        List<ClientEntity> clients = clientRepository.findAll();

        ArrayList<ClientEntity> restrictedClients = new ArrayList<>();
        for (ClientEntity client: clients) {
            if (client.getStateClient().equals("RESTRINGIDO")){
                restrictedClients.add(client);
            }
        }
        return restrictedClients;
    }
}
