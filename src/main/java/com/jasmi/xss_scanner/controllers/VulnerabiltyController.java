package com.jasmi.xss_scanner.controllers;


import com.jasmi.xss_scanner.dtos.VulnerabilityInputDto;
import com.jasmi.xss_scanner.dtos.VulnerabilityOutputDto;
import com.jasmi.xss_scanner.models.Vulnerability;
import com.jasmi.xss_scanner.services.VulnerabilityService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/xss_scanner_api/vulnerabilities")
public class VulnerabiltyController {

    private final VulnerabilityService vulnerabilityService;

    public VulnerabiltyController(VulnerabilityService vulnerabilityService) {
        this.vulnerabilityService = vulnerabilityService;
    }

    @GetMapping()
    public ResponseEntity<List<VulnerabilityOutputDto>> getVulnerabilities() {
        return ResponseEntity.ok(vulnerabilityService.getAllVulnerabilities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VulnerabilityOutputDto> getVulnerability(@PathVariable long id){
        return ResponseEntity.ok(vulnerabilityService.getVulnarabilityById(id));
    }

    @PostMapping()
    public ResponseEntity<VulnerabilityOutputDto> addVulnerability(@Valid @RequestBody VulnerabilityInputDto vulnerability) {
        VulnerabilityOutputDto v = vulnerabilityService.saveVulnerability(vulnerability);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(v.id).toUri();
        return ResponseEntity.created(location).body(v);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateVulnerability(@Valid @PathVariable long id, @RequestBody VulnerabilityInputDto vulnerability) {
        vulnerabilityService.updateVulnerability(id, vulnerability);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVulnerability(@PathVariable long id) {
        vulnerabilityService.deleteVulnerability(id);
        return ResponseEntity.noContent().build();
    }


}
