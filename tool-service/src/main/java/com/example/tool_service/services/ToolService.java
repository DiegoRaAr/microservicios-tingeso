package com.example.tool_service.services;

import com.example.tool_service.entities.ToolEntity;
import com.example.tool_service.model.Kardex;
import com.example.tool_service.repository.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class ToolService {
    @Autowired
    ToolRepository toolRepository;

    @Autowired
    RestTemplate restTemplate;

    // Find Tools
    public ArrayList<ToolEntity> getTools() {
        return (ArrayList<ToolEntity>) toolRepository.findAll();
    }

    // Create Tool
    @Transactional
    public ToolEntity createTool(ToolEntity toolEntity) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ToolEntity newTool = toolRepository.save(toolEntity);

        Kardex kardex = new Kardex();
        kardex.setDateKardex(new java.util.Date());
        kardex.setStateTool("CREACIÓN");
        kardex.setIdTool(newTool.getIdTool());
        kardex.setNameTool(newTool.getNameTool());

        HttpEntity<Kardex> request = new HttpEntity<>(kardex, headers);
        restTemplate.postForObject("http://kardex-service/kardex/", request, Kardex.class);

        return newTool;
    }

    // Find Tool by id
    public ToolEntity findById(Long id) {
        return toolRepository.findById(id).get();
    }

    // Update Tool
    public ToolEntity updateTool(ToolEntity toolEntity) {
        return toolRepository.save(toolEntity);
    }

    // Delete Tool by Id
    public boolean deleteTool(Long id) throws Exception {
        try {
            toolRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    // subtract 1 by id
    @Transactional
    public boolean subtractTool(Long id_tool) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ToolEntity tool = toolRepository.findById(id_tool).get();
        if(tool == null){
            return false;
        }

        int stock = tool.getStockTool();

        Kardex kardex = new Kardex();
        kardex.setDateKardex(new java.util.Date());
        kardex.setIdTool(tool.getIdTool());

        // if tool is last one
        if (stock <= 1){
            tool.setStockTool(stock - 1);
            tool.setStateTool("BAJA");

            kardex.setStateTool("BAJA");
            kardex.setNameTool(tool.getNameTool());

            HttpEntity<Kardex> request = new HttpEntity<>(kardex, headers);
            restTemplate.postForObject("http://kardex-service/kardex/", request, Kardex.class);

            toolRepository.save(tool);
            return true;
        }

        // if tool is not last one
        if (tool.getStockTool() > 1){
            tool.setStockTool(tool.getStockTool() - 1);

            kardex.setStateTool("DISMINUCIÓN");
            kardex.setNameTool(tool.getNameTool());

            HttpEntity<Kardex> request = new HttpEntity<>(kardex, headers);
            restTemplate.postForObject("http://kardex-service/kardex/", request, Kardex.class);

            toolRepository.save(tool);
            return true;
        }

        System.out.println("Action not found (subtract one)");
        return false;
    }

    // Add tool by id
    public boolean addTool(Long id){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ToolEntity tool =  findById(id);
        Kardex kardex = new Kardex();

        // Update the kardex
        kardex.setDateKardex(new java.util.Date());
        kardex.setIdTool(tool.getIdTool());
        kardex.setStateTool("SUMA");
        kardex.setNameTool(tool.getNameTool());

        HttpEntity<Kardex> request = new HttpEntity<>(kardex, headers);
        restTemplate.postForObject("http://kardex-service/kardex/", request, Kardex.class);

        tool.setStockTool(tool.getStockTool() + 1);
        toolRepository.save(tool);
        return true;
    }
}