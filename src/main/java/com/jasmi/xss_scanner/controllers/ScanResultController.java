package com.jasmi.xss_scanner.controllers;

import com.jasmi.xss_scanner.dtos.scanresult.ScanResultOutputDto;
import com.jasmi.xss_scanner.services.ScanResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/scan_result/{id}")
    public ResponseEntity<Void> deleteScanResult(@PathVariable Long id) {
        scanResultService.deleteScanResult(id);
        return ResponseEntity.noContent().build();
    }

}
