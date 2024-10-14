package com.jasmi.xss_scanner.controllers;

import com.jasmi.xss_scanner.dtos.ScanRequestInputDto;
import com.jasmi.xss_scanner.dtos.ScanRequestOutputDto;
import com.jasmi.xss_scanner.models.ScanRequest;
import com.jasmi.xss_scanner.models.ScanResult;
import com.jasmi.xss_scanner.services.ScanRequestService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/xss_scanner_api/scanrequest")
public class ScanRequestController {

    private final ScanRequestService scanRequestService;

    public ScanRequestController(ScanRequestService scanRequestService) {
        this.scanRequestService = scanRequestService;
    }

    @GetMapping()
    public ResponseEntity<List<ScanRequestOutputDto>> getAllScanRequests() {
        return ResponseEntity.ok(scanRequestService.getAllScanRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScanRequestOutputDto> getScanRequestById(@PathVariable long id){
        return ResponseEntity.ok(scanRequestService.getScanRequestById(id));
    }

    @PostMapping()
    public ResponseEntity<ScanRequestOutputDto> addScanRequest(@Valid @RequestBody ScanRequestInputDto scanRequest) {
        ScanRequestOutputDto t = scanRequestService.saveScanRequest(scanRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(t.getId()).toUri();
        return ResponseEntity.created(location).body(t);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Void> updateScanRequest(@PathVariable long id, @RequestBody ScanRequestInputDto scanRequest) {
//         scanRequestService.updateScanRequest(id, scanRequest);
//         return ResponseEntity.noContent().build();
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScanRequest(@PathVariable long id) {
        scanRequestService.deleteScanRequest(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/scan_result")
    public ResponseEntity<String> getScanResult(@PathVariable long id) {
        ScanRequestOutputDto scanRequestDto = scanRequestService.getScanRequestById(id);
        if (scanRequestDto.getScanResult() != null) {
            return ResponseEntity.ok(scanRequestDto.getScanResult().toString());//.getResultData());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
