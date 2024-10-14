package com.jasmi.xss_scanner.controllers;

import com.jasmi.xss_scanner.dtos.SolutionInputDto;
import com.jasmi.xss_scanner.dtos.SolutionOutputDto;
import com.jasmi.xss_scanner.services.SolutionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/xss_scanner_api/solutions")
public class SolutionController {

    private final SolutionService solutionService;

    public SolutionController(SolutionService solutionService) {
        this.solutionService = solutionService;
    }

    @GetMapping()
    public ResponseEntity<List<SolutionOutputDto>> getSolutions() {
        return ResponseEntity.ok(solutionService.getAllSolutions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolutionOutputDto> getSolution(@PathVariable long id){
        return ResponseEntity.ok(solutionService.getSolutionById(id));
    }

    @PostMapping()
    public ResponseEntity<SolutionOutputDto> addSolution(@Valid @RequestBody SolutionInputDto solution) {
        SolutionOutputDto s = solutionService.saveSolution(solution);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(s.getId()).toUri();
        return ResponseEntity.created(location).body(s);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSolution(@Valid @PathVariable long id, @RequestBody SolutionInputDto solution) {
        solutionService.updateSolution(id, solution);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSolution(@PathVariable long id) {
        solutionService.deleteSolution(id);
        return ResponseEntity.noContent().build();
    }
}
