package com.jasmi.xss_scanner.controllers;

import com.jasmi.xss_scanner.dtos.ScanRequestInputDto;
import com.jasmi.xss_scanner.dtos.ScanRequestOutputDto;
import com.jasmi.xss_scanner.models.ScanRequest;
import com.jasmi.xss_scanner.models.ScanResult;
import com.jasmi.xss_scanner.services.ScanRequestService;
import jakarta.servlet.Servlet;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Objects;

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

    @PostMapping("/{id}/screenshot")
    public ResponseEntity<ScanRequestOutputDto> addScreenshotToScanRequest(@PathVariable("id") Long id,
                                                                          @RequestParam("file") MultipartFile file) throws IOException {

        ScanRequestOutputDto t = scanRequestService.getScanRequestById(id);

        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        byte[] screenshot = file.getBytes();

        ScanRequestOutputDto scanRequest = scanRequestService.addScreenshotToScanRequest(screenshot, fileName, contentType, id);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("{id}/screenshot")
                .buildAndExpand(scanRequest.getId())
                .toUri();

        return ResponseEntity.created(location).body(scanRequest);
    }

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
