package com.example.tool_service.controller;

import com.example.tool_service.entities.ToolEntity;
import com.example.tool_service.services.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tool")
public class ToolController {
    @Autowired
    ToolService toolService;

    // Get all tools
    @GetMapping("/")
    public ResponseEntity<List<ToolEntity>> listTool() {
        List<ToolEntity> tools = toolService.getTools();
        return ResponseEntity.ok(tools);
    }

    // Get tool by id
    @GetMapping("/{id}")
    public ResponseEntity<ToolEntity> getToolById(@PathVariable Long id) {
        ToolEntity tool = toolService.findById(id);
        return ResponseEntity.ok(tool);
    }

    // Create tool
    @PostMapping("/")
    public ResponseEntity<ToolEntity> saveTool(@RequestBody ToolEntity tool) {
        ToolEntity newTool = toolService.createTool(tool);
        return ResponseEntity.ok(newTool);
    }

    // Update tool
    @PutMapping("/")
    public ResponseEntity<ToolEntity> updateTool(@RequestBody ToolEntity tool) {
        ToolEntity toolUpdated = toolService.updateTool(tool);
        return ResponseEntity.ok(toolUpdated);
    }

    // Delete tool by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteTool(@PathVariable Long id) throws Exception {
        var isDeleted = toolService.deleteTool(id);
        return ResponseEntity.noContent().build();
    }

    // Subtract tool number
    @PutMapping("/subtract-tool/{id}")
    public   ResponseEntity<Boolean> subtractToolNumber(@PathVariable Long id) throws Exception {
        toolService.subtractTool(id);
        return ResponseEntity.noContent().build();
    }

    // Add tool number
    @PutMapping("/add-tool/{id}")
    public   ResponseEntity<Boolean> addToolNumber(@PathVariable Long id) throws Exception {
        toolService.addTool(id);
        return ResponseEntity.noContent().build();
    }

    // Get best tools by range date
    @GetMapping("/best-tools-by-range-date/{initDate}/{endDate}")
    public ResponseEntity<List<ToolEntity>> getBestToolsByRangeDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.util.Date initDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.util.Date endDate
    ) {
        List<ToolEntity> bestTools = toolService.getBestToolsByRangeDate(initDate, endDate);
        return ResponseEntity.ok(bestTools);
    }
}