package com.jasmi.xss_scanner.controllers;

import com.jasmi.xss_scanner.dtos.ScanRequestInputDto;
import com.jasmi.xss_scanner.dtos.ScanRequestOutputDto;
import com.jasmi.xss_scanner.services.ScanRequestService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/xss_scanner_api")
public class ScanRequestController {

    private final ScanRequestService scanRequestService;

    public ScanRequestController(ScanRequestService scanRequestService) {
        this.scanRequestService = scanRequestService;
    }

    @GetMapping("/scan_requests")
    public ResponseEntity<List<ScanRequestOutputDto>> getAllScanRequests() {
        return ResponseEntity.ok(scanRequestService.getAllScanRequests());
    }

    @GetMapping("/scan_request/{id}")
    public ResponseEntity<ScanRequestOutputDto> getScanRequestById(@PathVariable long id){
        return ResponseEntity.ok(scanRequestService.getScanRequestById(id));
    }

    @PostMapping("/scan_request")
    public ResponseEntity<ScanRequestOutputDto> addScanRequest(@Valid @RequestBody ScanRequestInputDto scanRequest) {
        ScanRequestOutputDto t = scanRequestService.saveScanRequest(scanRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(t.getId())
                .toUri();

        return ResponseEntity.created(location).body(t);
    }

    @PostMapping("/scan_request/{id}/screenshot")
    public ResponseEntity<ScanRequestOutputDto> addScreenshotToScanRequest(@PathVariable("id") Long id,
                                                                          @RequestParam("file") MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        byte[] screenshot = file.getBytes();

        ScanRequestOutputDto scanRequest = scanRequestService.addScreenshotToScanRequest(screenshot, fileName, contentType, id);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .buildAndExpand(scanRequest.getId())
                .toUri();

        return ResponseEntity.created(location).body(scanRequest);
    }

    @GetMapping("/scan_request/{id}/screenshot")
    public ResponseEntity<byte[]> getScanRequestScreenshot(@PathVariable("id") Long id) {
        byte[] screenshot = scanRequestService.getScreenshotById(id);

        if(screenshot == null || screenshot.length == 0) {
            return ResponseEntity.notFound().build();
        }

        ScanRequestOutputDto scanRequest = scanRequestService.getScanRequestById(id);

        MediaType mediaType;
        try {
            mediaType = MediaType.parseMediaType(scanRequest.getScreenshotFileType());
        } catch (InvalidMediaTypeException ignore){
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity
                .ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + scanRequest.getScreenshotFileName())
                .body(screenshot);
    }

    @DeleteMapping("/scan_request/{id}")
    public ResponseEntity<Void> deleteScanRequest(@PathVariable long id) {
        scanRequestService.deleteScanRequest(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/scan_request/{id}/scan_result")
    public ResponseEntity<String> getScanResult(@PathVariable long id) {
        ScanRequestOutputDto scanRequestDto = scanRequestService.getScanRequestById(id);
        if (scanRequestDto.getScanResult() != null) {
            return ResponseEntity.ok(scanRequestDto.getScanResult().toString());//.getResultData());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
