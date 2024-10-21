package com.jasmi.xss_scanner.controllers;

import com.jasmi.xss_scanner.dtos.ScanResultInputDto;
import com.jasmi.xss_scanner.dtos.ScanResultOutputDto;
import com.jasmi.xss_scanner.services.ScanResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/xss_scanner_api")
public class ScanResultController {

    private final ScanResultService scanResultService;

    public ScanResultController(ScanResultService scanResultService) {
        this.scanResultService = scanResultService;
    }

    @GetMapping("/scan_results")
    public ResponseEntity<List<ScanResultOutputDto>> getScanResults() {
        return ResponseEntity.ok(scanResultService.getAllScanResults());
    }

    @GetMapping("/scan_result/{id}")
    public ResponseEntity<ScanResultOutputDto> getScanResult(@PathVariable Long id) {
        return ResponseEntity.ok(scanResultService.getScanResultById(id));
    }

    @PutMapping("/scan_result/{id}")
    public ResponseEntity<Void> updateScanResult(@PathVariable Long id, @RequestBody ScanResultInputDto scanResultInputDto) {
        scanResultService.updateScanResult(id, scanResultInputDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/scan_result/{id}")
    public ResponseEntity<Void> deleteScanResult(@PathVariable Long id) {
        scanResultService.deleteScanResult(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/scan_result/{scanResultId}/vulnerabilities/{vulnerabilityId}")
    public ResponseEntity<Void> assignVulnerabilityToScanResult(@PathVariable long scanResultId,@PathVariable long vulnerabilityId) {
        scanResultService.assignVulnerabilityToScanResult(scanResultId, vulnerabilityId);
        return ResponseEntity.noContent().build();
    }

}
