package com.jasmi.xss_scanner.controllers;

import com.jasmi.xss_scanner.dtos.solution.SolutionInputDto;
import com.jasmi.xss_scanner.dtos.solution.SolutionOutputDto;
import com.jasmi.xss_scanner.services.SolutionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/xss_scanner_api")
public class SolutionController {

    private final SolutionService solutionService;

    public SolutionController(SolutionService solutionService) {
        this.solutionService = solutionService;
    }

    @GetMapping("/solutions")
    public ResponseEntity<List<SolutionOutputDto>> getSolutions() {
        return ResponseEntity.ok(solutionService.getAllSolutions());
    }

    @GetMapping("/solution/{id}")
    public ResponseEntity<SolutionOutputDto> getSolution(@PathVariable long id){
        return ResponseEntity.ok(solutionService.getSolutionById(id));
    }

    @PostMapping("/solution")
    public ResponseEntity<SolutionOutputDto> addSolution(@Valid @RequestBody SolutionInputDto solution) {
        SolutionOutputDto s = solutionService.saveSolution(solution);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(s.getId()).toUri();
        return ResponseEntity.created(location).body(s);
    }

    @PutMapping("/solution/{id}")
    public ResponseEntity<Void> updateSolution(@Valid @PathVariable long id, @RequestBody SolutionInputDto solution) {
        solutionService.updateSolution(id, solution);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/solution/{id}")
    public ResponseEntity<Void> deleteSolution(@PathVariable long id) {
        solutionService.deleteSolution(id);
        return ResponseEntity.noContent().build();
    }
}
