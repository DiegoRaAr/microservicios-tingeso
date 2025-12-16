package com.example.tool_service.repository;

import com.example.tool_service.entities.ToolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolRepository extends JpaRepository<ToolEntity, Long> {
    // Function to find tool by name.
    // Input: name of tool(String)
    // Output: tool entity
    public ToolEntity findByNameTool(String name);

    // Function to find tool by id.
    // Input: id of tool(Long)
    // Output: tool entity
    public ToolEntity findByIdTool(long id);

    // Function to find tool by category.
    // Input: category of tool(String)
    // Output: list of tool entities
    List<ToolEntity> findByCategoryTool(String category);

    // Function to find tool by state.
    // Input: state of tool(String)
    // Output: list of tool entities
    List<ToolEntity> findByStateTool(String state);

    // Function to find tool by daily charge.
    // Input: daily charge of tool(int)
    // Output: list of tool entities
    List<ToolEntity> findByDailyCharge(int daily_charge);

}